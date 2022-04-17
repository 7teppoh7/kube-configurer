package ru.sstu.hobbyboard.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sstu.hobbyboard.entities.Maker;
import ru.sstu.hobbyboard.exceptionsOLD.ResourceNotFoundException;
import ru.sstu.hobbyboard.servicies.MakerService;

@Controller
@RequestMapping("/maker")
public class MakerController {

    private final MakerService makerService;


    public MakerController(MakerService makerService) {
        this.makerService = makerService;
    }

    @GetMapping("/{maker}")
    public String getMaker(Model model, @PathVariable Maker maker){
        if (maker == null) throw new ResourceNotFoundException("Производитель не найден");
        model.addAttribute("maker", maker);
        return "maker";
    }
}
