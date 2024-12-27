package com.carwash.CarWash.repository;

import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByServiceType(ServiceType serviceType);// Find services by type
}

