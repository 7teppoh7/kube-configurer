package ru.sstu.kubeconfigurerui.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.kubeconfigurerui.entity.ConfigurationHistory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConfigurationHistoryRepository extends JpaRepository<ConfigurationHistory, UUID> {

    Optional<ConfigurationHistory> findFirstByConfigurationIdAndServiceId(UUID configuration, UUID service, Sort sort);

    Optional<ConfigurationHistory> findFirstByServiceId(UUID serviceId, Sort sort);

    List<ConfigurationHistory> findByConfigurationId(UUID configId, Sort sort);

    List<ConfigurationHistory> findByServiceId(UUID serviceId, Sort sort);
}
