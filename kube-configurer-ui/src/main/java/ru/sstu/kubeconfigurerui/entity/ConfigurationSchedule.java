package ru.sstu.kubeconfigurerui.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationSchedule {

    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime startDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private Configuration configuration;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Service> services;
}
