package ru.sstu.hobbyboard.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sstu.hobbyboard.entities.*;
import ru.sstu.hobbyboard.entities.dto.NewPurchaseDTO;
import ru.sstu.hobbyboard.exceptionsOLD.MyForbiddenException;
import ru.sstu.hobbyboard.exceptionsOLD.ResourceNotFoundException;
import ru.sstu.hobbyboard.servicies.PurchaseCompositionService;
import ru.sstu.hobbyboard.servicies.PurchaseService;
import ru.sstu.hobbyboard.servicies.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

@Controller
public class BuyController {

    private final UserService userService;
    private final PurchaseCompositionService purchaseCompositionService;
    private final PurchaseService purchaseService;

    public BuyController(UserService userService, PurchaseCompositionService purchaseCompositionService, PurchaseService purchaseService) {
        this.userService = userService;
        this.purchaseCompositionService = purchaseCompositionService;
        this.purchaseService = purchaseService;
    }

    @PostMapping("/buyAll")
    public String buyAll(@AuthenticationPrincipal User user, Model model, HttpSession httpSession) {
        Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
        if (baskets != null) {
            baskets.forEach((x) -> x.setUser(user));
            if (user.getBaskets().addAll(baskets))
                model.addAttribute("message", "В ваш заказ были добавлены товары, которые были у вас в корзине");
        }
        userService.save(user);
        model.addAttribute("products", user.getBaskets());
        return "redirect:/buy";
    }

    @PostMapping("/buy")
    public String buy(@AuthenticationPrincipal User user, Product product, Integer countToBuy, Model model) {
        if (product == null) throw new ResourceNotFoundException("Продукт не найден");
        if (countToBuy == null) countToBuy = 1;
        user = userService.findByEmail(user.getEmail());
        user.getBaskets().clear();
        user = userService.save(user);
        user.getBaskets().add(new Basket(user, product, countToBuy));
        userService.save(user);
        model.addAttribute("products", user.getBaskets());
        return "redirect:/buy";
    }

    @GetMapping("/buy")
    public String getBuy(@AuthenticationPrincipal User user, Model model) {
        user = userService.findByEmail(user.getEmail());
        model.addAttribute("products", user.getBaskets());
        model.addAttribute("finalPrice", user.getBaskets().stream()
                .mapToDouble(x -> x.getProduct().getPrice() * x.getCount())
                .reduce(0, Double::sum));
        model.addAttribute("newPurchaseDTO", new NewPurchaseDTO(user.getFirstName(), user.getLastName(), user.getPhone()));
        return "orderCheck";
    }

    @PostMapping("/buy/commit")
    public String commit(@AuthenticationPrincipal User user, @ModelAttribute("purchaseDto") @Valid NewPurchaseDTO newPurchaseDTO) {
        user = userService.findByEmail(user.getEmail());
        user.setFirstName(newPurchaseDTO.getFirstName());
        user.setLastName(newPurchaseDTO.getLastName());
        user.setPhone(newPurchaseDTO.getPhone());
        user = userService.save(user);
        Purchase purchase = new Purchase();
        purchase.setAddress(newPurchaseDTO.getAddress());
        purchase.setBonusPaid(0F);
        double sum = user.getBaskets().stream().mapToDouble(x -> x.getProduct().getPrice() * x.getCount()).sum();
        purchase.setBonusGet((float) ((sum / 100) * user.getCashBack()));
        Set<PurchaseComposition> compositions = purchaseCompositionService.getCompositionFromBasket(user.getBaskets());
        purchase.setCompositions(compositions);
        purchase.setPrice(0F);
        purchase.setState(State.WAIT);
        purchase.setTotal(purchaseCompositionService.getPrice(compositions));
        purchase.setUser(user);
        Purchase saved = purchaseService.save(purchase);
        return "redirect:/pay?purchase=" + saved.getId();
    }

    @GetMapping("/pay")
    public String payForPurchase(@RequestParam Purchase purchase, @AuthenticationPrincipal User user, Model model) {
        if (purchase == null) throw new ResourceNotFoundException("Такой покупки не существует");
        if (!purchaseService.isUserPurchase(user.getId(), purchase))
            throw new MyForbiddenException("У вас нет доступа к этой покупке");
        user = userService.findByEmail(user.getEmail());
        model.addAttribute("purchase", purchase);
        model.addAttribute("max", user.getBonus() > purchase.getTotal() ? purchase.getTotal() : user.getBonus());
        model.addAttribute("cashBack", user.getCashBack());
        return "pay";
    }

    @PostMapping("/pay/cancel")
    public String cancelPurchase(Purchase bonuses, Purchase purchase, @AuthenticationPrincipal User user) {
        if (purchase == null) throw new ResourceNotFoundException("Покупка не существует");
        user = userService.findByEmail(user.getEmail());
        if (!purchaseService.isUserPurchase(user.getId(), purchase))
            throw new MyForbiddenException("У вас нет доступа к этой покупке");
        user.getPurchases().remove(purchase);
        userService.save(user);
        return "redirect:/";
    }

    @PostMapping("/pay/commit")
    public String commitPurchase(Model model, Purchase bonuses, Purchase purchase, @AuthenticationPrincipal User user) {
        user = userService.findByEmail(user.getEmail());
        if (purchase == null) throw new ResourceNotFoundException("Покупка не существует");
        if (!purchaseService.isUserPurchase(user.getId(), purchase))
            throw new MyForbiddenException("У вас нет доступа к этой покупке");
        if (bonuses.getBonusPaid() > user.getBonus() || bonuses.getBonusPaid() > purchase.getTotal()) {
            model.addAttribute("purchase", bonuses);
            model.addAttribute("max", user.getBonus() > bonuses.getTotal() ? bonuses.getTotal() : user.getBonus());
            model.addAttribute("error", "Невозможно списать такое количество бонусов");
            model.addAttribute("cashBack", user.getCashBack());
            return "pay";
        }
        user.setBonus(user.getBonus() - bonuses.getBonusPaid() + bonuses.getBonusGet());
        userService.save(user);
        purchase.setTotal(purchase.getTotal() - bonuses.getBonusPaid());
        purchase.setState(State.PAID);
        purchaseService.save(purchase);
        return "commit";
    }

    @GetMapping("/pay/info")
    public String info(Model model, @RequestParam Purchase purchase, @AuthenticationPrincipal User user) {
        user = userService.findByEmail(user.getEmail());
        if (purchase == null) throw new ResourceNotFoundException("Покупка не существует");
        if (!purchaseService.isUserPurchase(user.getId(), purchase))
            throw new MyForbiddenException("У вас нет доступа к этой покупке");
        model.addAttribute("purchase", purchase);
        model.addAttribute("cashBack", user.getCashBack());
        return "payInfo";
    }
}
