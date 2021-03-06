package ru.sstu.kubeconfigurerui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private Integer cpuRequest;
    private Double cpuLimit;
    private Integer memoryRequest;
    private Integer memoryLimit;
    private String runArgs;
    private Integer replicas;
    //todo: configuration history for additional reports
    private LocalDateTime lastUpdate;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Tag> tags;
}