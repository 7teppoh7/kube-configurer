package ru.sstu.kubeconfigurerui.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sstu.kubeconfigurerui.dto.LastActionDTO;
import ru.sstu.kubeconfigurerui.dto.ServiceDTO;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    @Id
    @GeneratedValue
    private UUID id;
    private UUID paasId;
    private String namespace;
    private String deploymentName;
    @ManyToOne(fetch = FetchType.EAGER)
    private Configuration configuration;
    @ElementCollection
    @CollectionTable(name = "service_tag_mapping",
            joinColumns = {@JoinColumn(name = "service_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "tag_name")
    @Column(name = "tag_value")
    private Map<String, String> labels;

    @Transient
    private LastActionDTO lastAction;

    public Service(ServiceDTO service) {
        this.paasId = service.getId();
        this.namespace = service.getNamespace();
        this.deploymentName = service.getName();
        this.labels = service.getLabels();
    }
}
