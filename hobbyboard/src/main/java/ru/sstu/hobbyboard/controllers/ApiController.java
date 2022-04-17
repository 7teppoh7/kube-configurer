package ru.sstu.hobbyboard.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.hobbyboard.entities.*;
import ru.sstu.hobbyboard.servicies.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ApiController {

    private final ProductService productService;
    private final MakerService makerService;
    private final UserService userService;
    private final PurchaseService purchaseService;
    private final CategoryService categoryService;

    @PostMapping("/product/api/products")
    public List<String> getProducts() {
        List<String> names = new ArrayList<>();
        for (Product p : productService.findAll()) {
            names.add(p.getName());
            names.add(p.getOriginalName());
        }
        return names;
    }

    @PostMapping("/product/api/search")
    public ResponseEntity<?> getSearchResultViaAjax(
            @Valid @RequestBody SearchCriteria search, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        List<Product> products = productService.findAllByName(search.getName().trim());
        if (products.isEmpty()) {
            result.setMsg("Продукт не найден");
            return ResponseEntity.badRequest().build();
        } else {
            result.setMsg("Успешно!");
            result.setResult(products.get(0));
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/product/api/search/id")
    public ResponseEntity<?> getSearchResultViaAjaxId(
            @Valid @RequestBody SearchCriteriaId search, Errors errors) {

        AjaxResponseBody result = new AjaxResponseBody();
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        Product product = productService.findById(search.getId());
        if (product == null) {
            result.setMsg("Продукт не найден");
            return ResponseEntity.badRequest().build();
        } else {
            result.setMsg("Успешно!");
            result.setResult(product);
            return ResponseEntity.ok(result);
        }
    }

    @PostMapping("/basket/api/inc")
    public Integer increase(@RequestBody Product product, HttpSession httpSession, @AuthenticationPrincipal User user) {
        if (product != null && product.getId() != null) product = productService.findById(product.getId());
        if (product != null) {
            if (user == null) {
                Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
                if (baskets != null) {
                    Product finalProduct = product;
                    Basket basket = baskets.stream().filter((x) -> x.getProduct().equals(finalProduct)).findFirst().orElse(null);
                    if (basket != null && basket.getCount() < basket.getProduct().getCount()) {
                        basket.setCount(basket.getCount() + 1);
                        return basket.getCount();
                    } else if (basket != null) return basket.getCount();
                }
            } else {
                user = userService.findByEmail(user.getEmail());
                Product finalProduct1 = product;
                Basket basket = user.getBaskets().stream().filter((x) -> x.getProduct().equals(finalProduct1)).findFirst().orElse(null);
                if (basket != null && basket.getCount() < basket.getProduct().getCount()) {
                    basket.setCount(basket.getCount() + 1);
                    userService.save(user);
                    return basket.getCount();
                } else if (basket != null) return basket.getCount();

            }
        }
        return 1;
    }

    @PostMapping("/basket/api/dec")
    public Integer decrease(@RequestBody Product product, HttpSession httpSession, @AuthenticationPrincipal User user) {
        if (product != null && product.getId() != null) product = productService.findById(product.getId());
        if (product != null) {
            if (user == null) {
                Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
                if (baskets != null) {
                    Product finalProduct = product;
                    Basket basket = baskets.stream().filter((x) -> x.getProduct().equals(finalProduct)).findFirst().orElse(null);
                    if (basket != null && basket.getCount() > 1) {
                        basket.setCount(basket.getCount() - 1);
                        return basket.getCount();
                    } else if (basket != null) return basket.getCount();
                }
            } else {
                user = userService.findByEmail(user.getEmail());
                Product finalProduct1 = product;
                Basket basket = user.getBaskets().stream().filter((x) -> x.getProduct().equals(finalProduct1)).findFirst().orElse(null);
                if (basket != null && basket.getCount() > 1) {
                    basket.setCount(basket.getCount() - 1);
                    userService.save(user);
                    return basket.getCount();
                } else if (basket != null) return basket.getCount();

            }
        }
        return 1;
    }

    @PostMapping("/basket/api/add")
    public void addProduct(@RequestBody Product product, HttpSession httpSession, @AuthenticationPrincipal User user) {
        if (product != null && product.getId() != null) product = productService.findById(product.getId());
        if (product != null) {
            if (user == null) {
                Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
                if (baskets != null) {
                    baskets.add(new Basket(product));
                    httpSession.setAttribute("baskets", baskets);
                }
            } else {
                user = userService.findByEmail(user.getEmail());
                user.getBaskets().add(new Basket(user, product));
                userService.save(user);
            }
        }
    }

    @PostMapping("/basket/api/remove")
    public void removeProduct(@RequestBody Product product, HttpSession httpSession, @AuthenticationPrincipal User user) {
        if (product != null && product.getId() != null) product = productService.findById(product.getId());
        if (product != null) {
            if (user == null) {
                Set<Basket> baskets = (Set<Basket>) httpSession.getAttribute("baskets");
                if (baskets != null) {
                    Product finalProduct = product;
                    baskets = baskets.stream().filter((x) -> !x.getProduct().equals(finalProduct)).collect(Collectors.toSet());
                    httpSession.setAttribute("baskets", baskets);
                }
            } else {
                Product finalProduct1 = product;
                user = userService.findByEmail(user.getEmail());
                user.setBaskets(user.getBaskets().stream().filter((x) -> !x.getProduct().equals(finalProduct1)).collect(Collectors.toSet()));
                userService.save(user);
            }
        }
    }

    @PostMapping("/admin/purchase/api/changeStatus")
    public void changeStatus(@RequestBody @Valid StatusChanger statusChanger) {
        Purchase purchase = purchaseService.findById(statusChanger.getId());
        if (purchase != null) {
            State state;
            try {
                state = State.valueOf(statusChanger.state);
            } catch (Exception ex) {
                return;
            }
            purchase.setState(state);
            purchaseService.save(purchase);
        }
    }

    @PostMapping("/categories/api/data1")
    public List<Object[]> data1() {
        List<Object[]> data = new ArrayList<>();
        categoryService.findAll().forEach(x -> data.add(new Object[]{x.getName(), categoryService.countProducts(x.getId())}));
        return data;
    }

    @PostMapping("/categories/api/data2")
    public List<Object[]> data2() {
        List<Object[]> data = new ArrayList<>();
        List<Integer> ids = userService.findTop3();
        for (int i = 0; i < ids.size(); i++){
            data.add(new Object[]{userService.findById(ids.get(i)).getEmail(), purchaseService.countByUserId(ids.get(i))});
        }
        return data;
    }

    @PostMapping("/categories/api/data3")
    public List<Object[]> data3() {
        List<Object[]> data = new ArrayList<>();
        makerService.findAll().forEach(x -> data.add(new Object[]{x.getName(), productService.countProductByMakerId(x.getId())}));
        return data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SearchCriteria {
        @NotBlank
        private String name;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class StatusChanger {
        @NotBlank
        private String state;
        @NotNull
        private Integer id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SearchCriteriaId {
        @NotNull
        private Integer id;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AjaxResponseBody {
        private String msg;
        private Product result;
    }
}
