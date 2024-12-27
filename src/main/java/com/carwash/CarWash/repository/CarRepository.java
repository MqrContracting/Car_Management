package com.carwash.CarWash.repository;

import com.carwash.CarWash.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByRegNo(String regNo);// Find car by registration number
}
