package ru.sstu.kubeconfigurerpaasapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sstu.kubeconfigurerpaasapi.client.PaasClient;
import ru.sstu.kubeconfigurerpaasapi.dto.ConfigurationDto;
import ru.sstu.kubeconfigurerpaasapi.dto.DeploymentDto;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/paas")
@RestController
@RequiredArgsConstructor
public class PaasApiController {

    private final PaasClient paasClient;

    @GetMapping("/namespaces")
    public List<String> getNamespaces() {
        return paasClient.getNamespaceNames();
    }

    @GetMapping("/deployments")
    public List<DeploymentDto> getDeploymentsDto(@RequestParam(required = false) String namespace) {
        return paasClient.getDeployments(namespace);
    }

    @PostMapping("/configure/{deploymentId}")
    public DeploymentDto configureService(@PathVariable UUID deploymentId, @RequestBody ConfigurationDto configuration) {
        return paasClient.configureDeployment(deploymentId, configuration);
    }

    @GetMapping("/test1")
    public void watchTest1() {
        paasClient.testWatch();
    }
    @GetMapping("/test2")
    public void watchTest2() {
        paasClient.closeWatch();
    }
}
