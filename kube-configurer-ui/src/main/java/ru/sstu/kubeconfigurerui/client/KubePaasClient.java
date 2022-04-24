package ru.sstu.kubeconfigurerui.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.sstu.kubeconfigurerui.dto.ServiceDTO;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.entity.Service;
import ru.sstu.kubeconfigurerui.service.ConfigurationHistoryService;
import ru.sstu.kubeconfigurerui.service.PaasServiceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class KubePaasClient {

    private final RestTemplate restTemplate;
    private final PaasServiceService paasServiceService;
    private final ConfigurationHistoryService configurationHistoryService;

    private final String MOCK_UUID_1 = "702d2060-3719-4c76-a376-44afeac85b31",
            MOCK_UUID_2 = "b3c7608b-aa20-4603-9b13-bd96fc93a90f";

    @Value("${paas.api.url}")
    public String paasUrl;

    public List<String> getNamespaces() {
//        return List.of("namespace-1", "namespace-2");
        return restTemplate.getForObject(paasUrl + "/api/paas/namespaces", List.class);
    }

    public List<Service> getServices(String namespace) {
//        ServiceDTO serviceOne = ServiceDTO.builder()
//                .id(MOCK_UUID_1)
//                .cpuLimit(500)
//                .cpuRequest(100)
//                .memoryLimit(512)
//                .memoryRequest(256)
//                .replicas(1)
//                .name("test-app-1")
//                .runArgs(List.of("run start.sh", "-XX:UseSerialGC"))
//                .creationTimestamp(new Date().toString())
//                .namespace("namespace-1")
//                .labels(Map.of("lang", "java", "name", "test-app-1"))
//                .build();
//        ServiceDTO serviceTwo = ServiceDTO.builder()
//                .id(MOCK_UUID_2)
//                .cpuLimit(300)
//                .cpuRequest(150)
//                .memoryLimit(1024)
//                .memoryRequest(512)
//                .replicas(2)
//                .name("test-app-2")
//                .runArgs(List.of("run start.sh", "-zx:qweqwe!"))
//                .creationTimestamp(new Date().toString())
//                .namespace("namespace-2")
//                .labels(Map.of("lang", "go", "name", "test-app-2"))
//                .build();
//        if ("namespace-2".equals(namespace)) {
//            return List.of(serviceTwo);
//        } else if ("namespace-1".equals(namespace)) {
//            return List.of(serviceOne);
//        }
//        return List.of(serviceOne, serviceTwo);
        String resultUrl = paasUrl + "/api/paas/deployments";
        resultUrl = namespace == null ? resultUrl : resultUrl + "?namespace=" + namespace;
        ParameterizedTypeReference<List<ServiceDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<List<ServiceDTO>> responseEntity = restTemplate.exchange(resultUrl, HttpMethod.GET, null, responseType);
        List<ServiceDTO> services = responseEntity.getBody();
        assert services != null;
        return paasServiceService.createOrUpdate(services);
    }

    public void configureService(UUID serviceId, Configuration configuration) {
        Service service = paasServiceService.findByIdOrNotFound(serviceId);
        paasServiceService.setConfiguration(service, configuration);
        configurationHistoryService.saveOperation(service, configuration);
        //todo: dto with name and namespace for async cases
        System.out.println(restTemplate.postForObject(paasUrl + "/api/paas/configure/" + service.getPaasId(), configuration, String.class));
    }
}
