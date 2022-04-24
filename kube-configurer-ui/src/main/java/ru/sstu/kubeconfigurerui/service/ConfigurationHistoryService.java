package ru.sstu.kubeconfigurerui.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import ru.sstu.kubeconfigurerui.dto.LastActionDTO;
import ru.sstu.kubeconfigurerui.entity.ApplyStatus;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.entity.ConfigurationHistory;
import ru.sstu.kubeconfigurerui.entity.Service;
import ru.sstu.kubeconfigurerui.repository.ConfigurationHistoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ConfigurationHistoryService {

    private final ConfigurationHistoryRepository configurationHistoryRepository;

    public void saveOperation(Service service, Configuration configuration) {
        configurationHistoryRepository.save(new ConfigurationHistory(service, configuration));
    }

    public void completeOperation(Service service, Configuration configuration) {
        setStatusForServiceAndConfiguration(service.getId(), configuration.getId(), ApplyStatus.SUCCESS, null);
    }

    public void failOperation(Service service, Configuration configuration, String message) {
        setStatusForServiceAndConfiguration(service.getId(), configuration.getId(), ApplyStatus.FAILED, message);
    }

    private void setStatusForServiceAndConfiguration(UUID serviceId, UUID configId, ApplyStatus status, String message) {
        Optional<ConfigurationHistory> historyOptional = configurationHistoryRepository.findFirstByConfigurationIdAndServiceId(configId, serviceId, Sort.by("date").descending());
        if (historyOptional.isEmpty()) {
            log.warn("History not found for service {} and config {}", serviceId, configId);
            return;
        }
        ConfigurationHistory historyEntity = historyOptional.get();
        historyEntity.setStatus(status);
        if (message != null) {
            historyEntity.setMessage(message);
        }
        configurationHistoryRepository.save(historyEntity);
    }

    public List<ConfigurationHistory> findByConfigId(UUID configId) {
        return configurationHistoryRepository.findByConfigurationId(configId, Sort.by("date").descending());
    }

    public List<ConfigurationHistory> findByServiceId(UUID serviceId) {
        return configurationHistoryRepository.findByServiceId(serviceId, Sort.by("date").descending());

    }

    public ConfigurationHistory getLastByServiceId(UUID serviceId) {
        return configurationHistoryRepository.findFirstByServiceId(serviceId, Sort.by("date").descending()).orElse(null);
    }

    public List<Service> enrichLastAction(List<Service> services) {
        services.forEach(service -> {
            ConfigurationHistory lastAction = getLastByServiceId(service.getId());
            if (lastAction != null) {
                service.setLastAction(new LastActionDTO(lastAction));
            }
        });
        return services;
    }
}
