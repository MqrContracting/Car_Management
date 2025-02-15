package com.carwash.CarWash.repository;

import com.carwash.CarWash.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    long count(); // Méthode intégrée pour compter les enregistrements
    Optional<Client> findByPhoneNumber(String phoneNumber);
    Optional<Client> findByEmail(String email);
}
