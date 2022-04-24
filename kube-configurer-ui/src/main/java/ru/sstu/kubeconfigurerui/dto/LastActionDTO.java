package ru.sstu.kubeconfigurerui.dto;

import lombok.Data;
import ru.sstu.kubeconfigurerui.entity.ApplyStatus;
import ru.sstu.kubeconfigurerui.entity.ConfigurationHistory;

import java.time.LocalDateTime;

@Data
public class LastActionDTO {
    private LocalDateTime date;
    private ApplyStatus status;

    public LastActionDTO(ConfigurationHistory configurationHistory) {
        this.date = configurationHistory.getDate();
        this.status = configurationHistory.getStatus();
    }
}
