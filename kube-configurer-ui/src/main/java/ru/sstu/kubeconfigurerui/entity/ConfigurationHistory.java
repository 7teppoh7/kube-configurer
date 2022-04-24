package ru.sstu.kubeconfigurerui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @OneToOne
    private Configuration configuration;
    @OneToOne
    private Service service;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status;
    private String message;

    public ConfigurationHistory(Service service, Configuration configuration) {
        this.configuration = configuration;
        this.service = service;
        this.date = LocalDateTime.now();
        this.status = ApplyStatus.IN_PROGRESS;
    }
}
