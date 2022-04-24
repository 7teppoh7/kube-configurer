package ru.sstu.kubeconfigurerui.controller.thymeleaf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sstu.kubeconfigurerui.client.KubePaasClient;
import ru.sstu.kubeconfigurerui.dto.SearchDTO;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.service.ConfigurationHistoryService;
import ru.sstu.kubeconfigurerui.service.ConfigurationService;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConfigureController {

    private final ConfigurationService configurationService;
    private final KubePaasClient kubePaasClient;
    private final ConfigurationHistoryService configurationHistoryService;

    @GetMapping("/configure")
    public String getConfigurations(Model model, @Valid SearchDTO searchDTO) {
        model.addAttribute("isConfigure", true);
        model.addAttribute("configurations", configurationService.findAll());
        model.addAttribute("services", configurationHistoryService.enrichLastAction(kubePaasClient.getServices(null)));
        if (searchDTO.getSearch() != null && searchDTO.getSearch().trim().equals("")) searchDTO.setSearch(null);
        if (searchDTO.getSort() == null) searchDTO.setSort("");
        model.addAttribute("searchDto", searchDTO);
        model.addAttribute("config", new Configuration());
        model.addAttribute("namespaces", kubePaasClient.getNamespaces());
        model.addAttribute("dateFormatter", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("ru")));
        return "configure";
    }
}
