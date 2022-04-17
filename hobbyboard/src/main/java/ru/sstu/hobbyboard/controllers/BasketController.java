package ru.sstu.hobbyboard.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sstu.hobbyboard.entities.Basket;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.servicies.BasketService;
import ru.sstu.hobbyboard.servicies.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

@Controller
public class BasketController {

    private final BasketService basketService;
    private final UserService userService;

    public BasketController(BasketService basketService, UserService userService) {
        this.basketService = basketService;
        this.userService = userService;
    }

    @GetMapping("/basket")
    public String getBasket(Model model, @AuthenticationPrincipal User user, HttpSession httpSession) {
        Set<Basket> baskets;
        if (user == null) {
            model.addAttribute("message", "Для продолжения покупки потребуется авторизация");
            if (httpSession.getAttribute("baskets") != null) {
                baskets = (Set<Basket>) httpSession.getAttribute("baskets");
            } else baskets = new HashSet<>();
        } else {
            User finalUser = userService.findByEmail(user.getEmail());//TODO: узнать почему у связных сущностей id = null
            baskets = (Set<Basket>) httpSession.getAttribute("baskets");
            boolean flag = false;
            if (baskets != null) {
                baskets.forEach((x) -> x.setUser(finalUser));
                finalUser.setBaskets(new HashSet<>(finalUser.getBaskets()));
                if (finalUser.getBaskets().addAll(baskets))
                    model.addAttribute("message", "В вашу корзину добавлены товары, которые вы сохранили без авторизации");
                httpSession.setAttribute("baskets", new HashSet<Basket>());
            }
            userService.save(finalUser);
            baskets = finalUser.getBaskets();
        }
        model.addAttribute("baskets", baskets);
        return "basket";
    }
}
