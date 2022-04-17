package ru.sstu.hobbyboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.sstu.hobbyboard.entities.PhotoProduct;
import ru.sstu.hobbyboard.entities.User;
import ru.sstu.hobbyboard.entities.dto.UserDTO;
import ru.sstu.hobbyboard.servicies.ImageService;
import ru.sstu.hobbyboard.servicies.UserService;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model){
        model.addAttribute("user", userService.findByEmail(user.getEmail()));
        model.addAttribute("userDto", new UserDTO(user));
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateUser(@AuthenticationPrincipal User user, Model model, @ModelAttribute("userDto") @Valid UserDTO userDto,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userService.findByEmail(user.getEmail()));
            model.addAttribute("userDto", userDto);
            return "profile";
        }
        user.setPhone(userDto.getPhone());
        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) user.setPassword(userDto.getPassword());
        user.setBirth(userDto.getBirth());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setNickName(userDto.getNickName());
        if (!userDto.getAvatar().isEmpty()){
            user.setAvatar(imageService.saveImages(userDto).get(0));
        }
        if (!userService.validateUser(user)){
            model.addAttribute("user", userService.findByEmail(user.getEmail()));
            model.addAttribute("userDto", userDto);
            model.addAttribute("error", "Данное имя уже занято");
            return "profile";
        }
        userService.save(user);
        return "redirect:/profile";
    }
}
