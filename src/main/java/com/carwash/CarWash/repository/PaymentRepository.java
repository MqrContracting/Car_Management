package com.carwash.CarWash.repository;

//import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Payment;
import com.carwash.CarWash.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
//import java.util.Optional;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByClientId(Long clientId);

    List<Payment> findByCar_RegNo(String regNo);

    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Transaction> findPaymentsWithinDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT p FROM Payment p " +
       "JOIN FETCH p.client " +
       "JOIN FETCH p.car " +
       "JOIN FETCH p.service")
    List<Payment> findAllWithDetails();

}

