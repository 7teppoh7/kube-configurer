package ru.sstu.kubeconfigurerui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.kubeconfigurerui.entity.Service;

import java.util.Optional;
import java.util.UUID;

public interface PaasServiceRepository extends JpaRepository<Service, UUID> {

    Optional<Service> findByNamespaceAndDeploymentName(String namespace, String name);

    Optional<Service> findByPaasId(UUID id);
}
