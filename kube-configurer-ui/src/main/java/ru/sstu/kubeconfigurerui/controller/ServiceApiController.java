package ru.sstu.kubeconfigurerui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.kubeconfigurerui.client.KubePaasClient;
import ru.sstu.kubeconfigurerui.entity.Service;
import ru.sstu.kubeconfigurerui.service.ConfigurationHistoryService;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceApiController {

    private final KubePaasClient kubePaasClient;
    private final ConfigurationHistoryService configurationHistoryService;

    @GetMapping
    public List<Service> getAllServices(@RequestParam(required = false) String namespace) {
        return configurationHistoryService.enrichLastAction(kubePaasClient.getServices(namespace));
    }
}
