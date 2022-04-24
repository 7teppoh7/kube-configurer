package ru.sstu.kubeconfigurerui.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConfigureDTO {
    private UUID configId;
    private UUID serviceId;
}
