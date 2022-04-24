package ru.sstu.kubeconfigurerui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private UUID id;
    private String name;
    private String namespace;
    private Map<String, String> labels;
    private String creationTimestamp;
    private Integer memoryRequest;
    private Integer memoryLimit;
    private Integer cpuRequest;
    private Integer cpuLimit;
    private Integer replicas;
    private List<String> runArgs;
}