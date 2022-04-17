package ru.sstu.hobbyboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sstu.hobbyboard.entities.Role;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.entities.dto.SearchDTO;
import ru.sstu.hobbyboard.entities.dto.UserDTO;
import ru.sstu.hobbyboard.servicies.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ProductService productService;
    private final UserService userService;
    private final BasketService basketService;
    private final CategoryService categoryService;
    private final MakerService makerService;
    private final ImageService imageService;


    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }

    @GetMapping
    public String getMain(Model model, HttpSession httpSession, @AuthenticationPrincipal User user, @Valid SearchDTO searchDTO) {
        if (user != null) user = userService.findByEmail(user.getEmail());
        basketService.basketInit(model, httpSession, user);
        if (searchDTO.getSearch() != null && searchDTO.getSearch().trim().equals("")) searchDTO.setSearch(null);
        if (searchDTO.getSort() == null) searchDTO.setSort("");
        model.addAttribute("minimalPrice", productService.minPrice());
        model.addAttribute("maximumPrice", productService.maxPrice());
        model.addAttribute("maximumPlayers", productService.maxMaxPlayers());
        model.addAttribute("maximumTime", productService.maxMaxTime());
        model.addAttribute("searchDto", searchDTO);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("makers", makerService.findAll());
        model.addAttribute("products", productService.findAllBySearchDto(searchDTO));
        return "main";
    }

    @GetMapping("/welcome")
    public String getWelcome() {
        return "welcome";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@ModelAttribute("userDto") @Valid UserDTO userDto, Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            model.addAttribute("userDto", userDto);
            return "register";
        }
        User user = new User(userDto);
        if (!userService.validateUser(user)) {
            model.addAttribute("error", "Имя занято");
            return "register";
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email занят");
            return "register";
        }
        user.setAvatar(imageService.saveImages(userDto).get(0));
        user.setUserRoles(Set.of(Role.USER));
        userService.save(user);
        return "redirect:/profile";
    }
}
