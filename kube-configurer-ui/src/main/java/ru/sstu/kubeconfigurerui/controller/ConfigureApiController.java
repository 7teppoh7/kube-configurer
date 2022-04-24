package ru.sstu.kubeconfigurerui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.kubeconfigurerui.client.KubePaasClient;
import ru.sstu.kubeconfigurerui.dto.ConfigureDTO;
import ru.sstu.kubeconfigurerui.service.ConfigurationService;

@RestController
@RequiredArgsConstructor
public class ConfigureApiController {

    private final KubePaasClient kubePaasClient;
    private final ConfigurationService configurationService;

    @PostMapping("/api/configure")
    public void configureService(@RequestBody ConfigureDTO configureDTO) {
        kubePaasClient.configureService(configureDTO.getServiceId(), configurationService.findById(configureDTO.getConfigId()));
    }
}
