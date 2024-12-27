package com.carwash.CarWash.repository;

import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCar_RegNo(String regNo); // Find transactions by car registration number

    List<Transaction> findByService_ServiceType(ServiceType serviceType); // Find transactions by service type

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findTransactionsWithinDateRange(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}

