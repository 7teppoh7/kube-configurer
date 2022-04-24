package ru.sstu.kubeconfigurerpaasapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationDto {
    private UUID id;
    private String name;
    private Integer cpuRequest;
    private Double cpuLimit;
    private Integer memoryRequest;
    private Integer memoryLimit;
    private String runArgs;
    private Integer replicas;
}