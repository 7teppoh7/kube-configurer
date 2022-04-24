package ru.sstu.kubeconfigurerui.controller.thymeleaf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sstu.kubeconfigurerui.client.KubePaasClient;
import ru.sstu.kubeconfigurerui.dto.LastActionDTO;
import ru.sstu.kubeconfigurerui.entity.Service;
import ru.sstu.kubeconfigurerui.service.ConfigurationHistoryService;
import ru.sstu.kubeconfigurerui.service.PaasServiceService;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ServiceController {

    private final KubePaasClient kubePaasClient;
    private final PaasServiceService paasServiceService;
    private final ConfigurationHistoryService configurationHistoryService;

    @GetMapping("/services")
    public String getServicesPage(Model model, @RequestParam(required = false) String namespace) {
        model.addAttribute("namespaces", kubePaasClient.getNamespaces());
        model.addAttribute("services", configurationHistoryService.enrichLastAction(kubePaasClient.getServices(namespace)));
        model.addAttribute("dateFormatter", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.forLanguageTag("ru")));
        model.addAttribute("isServices", true);
        return "services";
    }

    @GetMapping("/service/{serviceId}")
    public String getConfiguration(Model model, @PathVariable UUID serviceId) {
        Service service = paasServiceService.findByIdOrNotFound(serviceId);
        service.setLastAction(new LastActionDTO(configurationHistoryService.getLastByServiceId(serviceId)));
        model.addAttribute("service", service);
        model.addAttribute("histories", configurationHistoryService.findByServiceId(serviceId));
        model.addAttribute("dateFormatter", DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.forLanguageTag("ru")));
        return "service";
    }


}
