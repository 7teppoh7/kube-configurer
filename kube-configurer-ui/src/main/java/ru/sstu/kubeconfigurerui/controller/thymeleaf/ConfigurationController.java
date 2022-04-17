package ru.sstu.kubeconfigurerui.controller.thymeleaf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sstu.kubeconfigurerui.dto.SearchDTO;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.service.ConfigurationService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }

    @GetMapping
    public String redirectToConfigs() {
        return "redirect:/configurations";
    }

    @GetMapping("/configurations")
    public String getConfigurations(Model model, @Valid SearchDTO searchDTO) {
        if (searchDTO.getSearch() != null && searchDTO.getSearch().trim().equals("")) searchDTO.setSearch(null);
        if (searchDTO.getSort() == null) searchDTO.setSort("");
        model.addAttribute("minimalPrice", 0);
        model.addAttribute("maximumPrice", 100);
        model.addAttribute("maximumPlayers", 5);
        model.addAttribute("maximumTime", 10);
        model.addAttribute("searchDto", searchDTO);
        model.addAttribute("categories", new ArrayList<>());
        model.addAttribute("makers", new ArrayList<>());
        model.addAttribute("config", new Configuration());
        model.addAttribute("configurations", configurationService.findAll());
        return "configurations";
    }

    @GetMapping("/configuration/{configId}")
    public String getConfiguration(Model model, @PathVariable UUID configId) {
        model.addAttribute("config", configurationService.findById(configId));
        return "config";
    }

    @GetMapping("/deleteConfig/{configId}")
    public String deleteConfig(@PathVariable UUID configId) {
        configurationService.deleteById(configId);
        return "redirect:/configurations";
    }
}
