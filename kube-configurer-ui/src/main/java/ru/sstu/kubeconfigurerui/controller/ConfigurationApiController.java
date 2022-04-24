package ru.sstu.kubeconfigurerui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sstu.kubeconfigurerui.dto.ValidationResult;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.service.ConfigurationService;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
public class ConfigurationApiController {

    private final ConfigurationService configurationService;

    @PostMapping("/api/configuration/validation")
    public List<ValidationResult> validateConfig(@RequestBody Configuration configuration) {
        //todo: implement validation
        return new ArrayList<>();
    }

    @PostMapping("/api/configuration")
    public Configuration createOrUpdateConfig(@RequestBody Configuration configuration) {
        return configurationService.save(configuration);
    }

}
