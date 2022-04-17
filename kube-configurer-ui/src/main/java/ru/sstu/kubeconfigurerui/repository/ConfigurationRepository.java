package ru.sstu.kubeconfigurerui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sstu.kubeconfigurerui.entity.Configuration;

import java.util.UUID;

public interface ConfigurationRepository extends JpaRepository<Configuration, UUID> {
}
