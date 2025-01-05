package com.carwash.CarWash.repository;


import com.carwash.CarWash.entity.Enum.PaymentType;
import com.carwash.CarWash.entity.Payment;
import com.carwash.CarWash.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;



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

    @Query("SELECT SUM(p.givenPrice) FROM Payment p WHERE p.paymentType = :PaymentType")
    double sumGivenPriceByPaymentMethod(@Param("PaymentType") PaymentType PaymentType);



    // Total des paiements par jour
    @Query("SELECT FUNCTION('DATE', p.paymentDate) AS day, " +
            "SUM(p.givenPrice) " +
            "AS total FROM Payment p " +
            "GROUP BY FUNCTION('DATE', p.paymentDate)"+
            "ORDER BY FUNCTION('DATE', p.paymentDate) ASC")
    List<Object[]> getTotalPaymentsByDay();

    // Total des paiements par mois
    @Query("SELECT EXTRACT(MONTH FROM p.paymentDate) AS month, "+ "SUM(p.givenPrice) AS total "
         + "FROM Payment p "
         + "GROUP BY EXTRACT(MONTH FROM p.paymentDate) "
         + "ORDER BY EXTRACT(MONTH FROM p.paymentDate)")
    List<Object[]> getTotalPaymentsByMonth();

    @Query("SELECT EXTRACT(YEAR FROM p.paymentDate) AS year, SUM(p.givenPrice) AS total " +
           "FROM Payment p " +
           "GROUP BY EXTRACT(YEAR FROM p.paymentDate) " +
           "ORDER BY EXTRACT(YEAR FROM p.paymentDate) ASC")
    List<Object[]> getTotalPaymentsByYear();

}

