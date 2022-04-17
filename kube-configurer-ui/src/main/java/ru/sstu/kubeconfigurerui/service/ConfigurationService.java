package ru.sstu.kubeconfigurerui.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.repository.ConfigurationRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    public List<Configuration> findAll() {
        return configurationRepository.findAll();
    }

    public Configuration findById(UUID configId) {
        return configurationRepository.findById(configId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    public Configuration save(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public void deleteById(UUID configId) {
        configurationRepository.deleteById(configId);
    }
}
