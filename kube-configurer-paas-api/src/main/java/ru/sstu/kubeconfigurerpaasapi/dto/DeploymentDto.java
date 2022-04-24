package ru.sstu.kubeconfigurerpaasapi.dto;

import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import lombok.Data;

import java.util.List;
import java.util.Map;

import static ru.sstu.kubeconfigurerpaasapi.util.Constants.CPU;
import static ru.sstu.kubeconfigurerpaasapi.util.Constants.MEMORY;

@Data
public class DeploymentDto {

    private String id;
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

    public DeploymentDto(Deployment deployment) {
        this.name = deployment.getMetadata().getName();
        this.namespace = deployment.getMetadata().getNamespace();
        this.id = deployment.getMetadata().getUid();
        this.labels = deployment.getMetadata().getLabels();
        this.creationTimestamp = deployment.getMetadata().getCreationTimestamp();
        //todo: do smth with get(0)
        Map<String, Quantity> requests = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getResources().getRequests();
        Map<String, Quantity> limits = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getResources().getLimits();
        if (requests != null) {
            this.memoryRequest = requests.containsKey(MEMORY) ? Quantity.getAmountInBytes(requests.get(MEMORY)).intValue() : null;
            this.cpuRequest = requests.containsKey(CPU) ? Quantity.getAmountInBytes(requests.get(CPU)).intValue() : null;
        }
        if (limits != null) {
            this.memoryLimit = limits.containsKey(MEMORY) ? Quantity.getAmountInBytes(limits.get(MEMORY)).intValue() : null;
            this.cpuLimit = limits.containsKey(CPU) ? Quantity.getAmountInBytes(limits.get(CPU)).intValue() : null;
        }
        this.replicas = deployment.getSpec().getReplicas();
        this.runArgs = deployment.getSpec().getTemplate().getSpec().getContainers().get(0).getArgs();
    }
}
