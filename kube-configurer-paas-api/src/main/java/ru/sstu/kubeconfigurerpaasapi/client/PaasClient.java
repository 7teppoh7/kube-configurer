package ru.sstu.kubeconfigurerpaasapi.client;

import io.fabric8.kubernetes.api.model.LabelSelector;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.*;
import io.fabric8.kubernetes.client.dsl.FilterWatchListDeletable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.sstu.kubeconfigurerpaasapi.dto.ConfigurationDto;
import ru.sstu.kubeconfigurerpaasapi.dto.DeploymentDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.sstu.kubeconfigurerpaasapi.util.Constants.*;


@Slf4j
@Component
public class PaasClient {

    private final KubernetesClient kubernetesClient;

    public PaasClient() {
        this.kubernetesClient = new DefaultKubernetesClient();
    }

    public List<String> getNamespaceNames() {
        return kubernetesClient.namespaces().list().getItems().stream()
                .map(ns -> ns.getMetadata().getName())
                .collect(Collectors.toList());
    }

    public List<DeploymentDto> getDeployments(String namespace) {
        if (namespace != null) {
            return kubernetesClient.apps().deployments().inNamespace(namespace).list().getItems()
                    .stream()
                    .map(DeploymentDto::new)
                    .collect(Collectors.toList());
        } else {
            return kubernetesClient.apps().deployments().inAnyNamespace().list().getItems()
                    .stream()
                    .map(DeploymentDto::new)
                    .collect(Collectors.toList());
        }

    }

    //todo service entity
    public DeploymentDto configureDeployment(UUID deploymentId, ConfigurationDto configuration) {
        Deployment found = kubernetesClient.apps().deployments().list().getItems()
                .stream()
                .filter(d -> deploymentId.toString().equals(d.getMetadata().getUid()))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        ResourceRequirements resources = found.getSpec().getTemplate().getSpec().getContainers().get(0).getResources();
        Map<String, Quantity> requests = resources.getRequests();
        Map<String, Quantity> limits = resources.getLimits();
        requests = requests != null ? requests : new HashMap<>();
        requests.put(MEMORY, Quantity.parse(configuration.getMemoryRequest() + "Mi"));
        requests.put(CPU, Quantity.parse(configuration.getCpuRequest() + "m"));
        limits = limits != null ? limits : new HashMap<>();
        limits.put(MEMORY, Quantity.parse(configuration.getMemoryLimit() + "Mi"));
        limits.put(CPU, Quantity.parse(configuration.getCpuLimit() + "m"));
        found.getSpec().setReplicas(configuration.getReplicas());
        //todo: label for metadata
        resources.setLimits(limits);
        resources.setRequests(requests);
//        found.getSpec().getTemplate().getSpec().getContainers().get(0).getCommand().
        found.getMetadata().getLabels().put(CONFIG_NAME, configuration.getName());
        found.getMetadata().getLabels().put(CONFIG_ID, configuration.getId().toString());

        return new DeploymentDto(kubernetesClient.apps().deployments().replace(found));
    }

    private Watch watch;

    public void testWatch() {
        kubernetesClient.events().v1().events().inNamespace("default")
                .list().getItems()
                .stream()
                .filter(event -> event.getMetadata().getName().startsWith("zookeeper-7d8bb47bd4-wfgdx"))
                .forEach(System.out::println);
//        System.out.println(kubernetesClient.events().v1().events().inNamespace("default").withField("name", "zookeeper-95f58756d-hc8vf").list().getItems());
    }

    public void closeWatch() {
//        System.out.println(kubernetesClient.pods().inNamespace("default").withLabel("app:zookeeper").list().getItems());
        System.out.println(kubernetesClient.pods().inNamespace("default").withLabel("app=zookeeper").list().getItems());
//        System.out.println(kubernetesClient.pods().inNamespace("default").withLabel("app").list().getItems());
    }
}
