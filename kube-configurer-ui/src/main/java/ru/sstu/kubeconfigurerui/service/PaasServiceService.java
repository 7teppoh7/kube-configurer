package ru.sstu.kubeconfigurerui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.sstu.kubeconfigurerui.dto.ServiceDTO;
import ru.sstu.kubeconfigurerui.entity.Configuration;
import ru.sstu.kubeconfigurerui.entity.Service;
import ru.sstu.kubeconfigurerui.repository.PaasServiceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class PaasServiceService {

    private final PaasServiceRepository paasServiceRepository;

    public List<Service> createOrUpdate(List<ServiceDTO> services) {
        List<Service> allServices = new ArrayList<>();
        for (ServiceDTO service : services) {
            Service serviceToSaveOrUpdate;
            Optional<Service> serviceOptional = paasServiceRepository.findByPaasId(service.getId());
            if (serviceOptional.isEmpty()) {
                Optional<Service> serviceByNameOptional = paasServiceRepository.findByNamespaceAndDeploymentName(service.getNamespace(), service.getName());
                if (serviceByNameOptional.isPresent()) {
                    Service serviceFromDb = serviceByNameOptional.get();
                    log.info("Service {} was not found by id, but found by name and namespace {}, update id and labels", service.getName(), service.getNamespace());
                    serviceFromDb.setPaasId(service.getId());
                    serviceFromDb.setLabels(service.getLabels());
                    serviceToSaveOrUpdate = serviceFromDb;
                } else {
                    serviceToSaveOrUpdate = new Service(service);
                }
            } else {
                log.info("Service {} was found by paas id, update id and labels", service.getName());
                Service serviceFromDb = serviceOptional.get();
                serviceFromDb.setLabels(service.getLabels());
                serviceToSaveOrUpdate = serviceFromDb;
            }
            log.info("Service {} was created or updated", serviceToSaveOrUpdate);
            allServices.add(paasServiceRepository.save(serviceToSaveOrUpdate));
        }
        //need to not to return not existed service
        return allServices;
    }

    public Service findByIdOrNotFound(UUID serviceId) {
        return paasServiceRepository.findById(serviceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service " + serviceId + " not found!"));
    }

    public void setConfiguration(Service service, Configuration configuration) {
        service.setConfiguration(configuration);
        paasServiceRepository.save(service);
    }
}
