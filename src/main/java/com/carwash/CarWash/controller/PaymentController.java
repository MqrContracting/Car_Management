package com.carwash.CarWash.controller;


import com.carwash.CarWash.dtos.PaymentData;
import com.carwash.CarWash.entity.Payment;
import com.carwash.CarWash.entity.Transaction;
import com.carwash.CarWash.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<Payment> savePayment(@RequestBody Payment payment) {
        //System.out.println(payment);
        return ResponseEntity.ok(paymentService.savePayment(payment));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
    Optional<Payment> payment = paymentService.getPaymentById(id);

    return payment
            .map(ResponseEntity::ok) // Si trouvé, retourne 200 OK avec le paiement
            .orElseGet(() -> ResponseEntity.notFound().build()); // Si non trouvé, retourne 404 Not Found
}


    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }


    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getPaymentsWithinDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getPaymentsWithinDateRange(startDate, endDate));
    }

    // Endpoint pour récupérer le total des givenPrice
    @GetMapping("/total-income")
    public double getTotalIncome() {
        return paymentService.calculateTotalIncome();
    }

    // Endpoint pour récupérer le total des paiements en cash
    @GetMapping("/CASH")
    public double getCashPayments() {
        try {
        return paymentService.getCashPayments();
    } catch (Exception e) {
        // Gérer l'erreur ici, ou renvoyer une valeur par défaut
        throw new RuntimeException("Erreur lors du calcul des paiements en cash", e);
    }
    }

    // Endpoint pour récupérer le total des paiements via Juice
    @GetMapping("/JUICE")
    public double getJuicePayments() {
        return paymentService.getJuicePayments();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Payment>> getPaymentsByClient(@PathVariable Long clientId) {
        List<Payment> payments = paymentService.getPaymentsByClient(clientId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/by-day")
    public List<PaymentData> getPaymentsByDay() {
        return paymentService.getPaymentsByDay();
    }

    @GetMapping("/by-month")
    public List<PaymentData> getPaymentsByMonth() {
        return paymentService.getPaymentsByMonth();
    }

    @GetMapping("/by-year")
    public List<PaymentData> getPaymentsByYear() {
        return paymentService.getPaymentsByYear();
    }
     @PatchMapping("/{id}/status")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> updates) {

        try {
            String status = updates.get("status").toUpperCase();
            Payment updatedPayment = paymentService.updatePaymentStatus(id, status);
            System.out.println("Le Status est:"+ status);
            return ResponseEntity.ok(updatedPayment);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating payment status.");
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
}

