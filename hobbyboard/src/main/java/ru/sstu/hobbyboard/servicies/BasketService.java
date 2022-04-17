package ru.sstu.hobbyboard.servicies;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ru.sstu.hobbyboard.entities.Basket;
import ru.sstu.hobbyboard.entities.Product;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.repositories.BasketRepository;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BasketService {

    private final BasketRepository basketRepository;

    public BasketService(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    public Set<Basket> findBasketsByUserId(Integer id) {
        return basketRepository.findBasketsByUserId(id);
    }

    public void basketInit(Model model, HttpSession httpSession, User user) {
        if (user == null) {
            Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
            if (baskets == null) baskets = new HashSet<>();

            Set<Product> productsInBasket = baskets.stream().map(Basket::getProduct).collect(Collectors.toSet());

            httpSession.setAttribute("baskets", baskets);
            model.addAttribute("productsInBasket", productsInBasket);

        } else {
            Set<Product> products = user.getBaskets().stream().map(Basket::getProduct).collect(Collectors.toSet());
            model.addAttribute("productsInBasket", products);
        }
    }

    public Basket save(Basket basket) {
        return basketRepository.save(basket);
    }

    public void delete(Basket basket) {
        basketRepository.delete(basket);
    }
}
