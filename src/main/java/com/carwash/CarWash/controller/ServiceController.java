package com.carwash.CarWash.controller;

import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Service;
import com.carwash.CarWash.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<Service> saveService(@RequestBody Service service) {
        return ResponseEntity.ok(serviceService.saveService(service));
    }

    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        System.out.println(serviceService.getAllServices());
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/type/{serviceType}")
    public ResponseEntity<List<Service>> getServicesByType(@PathVariable ServiceType serviceType) {
        return ResponseEntity.ok(serviceService.getServiceByType(serviceType));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}

