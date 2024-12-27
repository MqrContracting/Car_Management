package com.carwash.CarWash.service;

import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public com.carwash.CarWash.entity.Service saveService(com.carwash.CarWash.entity.Service service) {
        return serviceRepository.save(service);
    }

    public Optional<com.carwash.CarWash.entity.Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public List<com.carwash.CarWash.entity.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public List<com.carwash.CarWash.entity.Service> getServiceByType(ServiceType serviceType) {
        return serviceRepository.findByServiceType(serviceType);
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
