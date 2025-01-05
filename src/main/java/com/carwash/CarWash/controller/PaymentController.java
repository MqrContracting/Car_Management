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
import java.util.*;

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

    private Map<String, Object> transformToChartData(List<PaymentData> results) {
        List<String> labels = new ArrayList<>();
        List<Number> data = new ArrayList<>();
        for (PaymentData result : results) {
            labels.add(result.getPeriod().toString()); // Date, Month, or Year
            data.add(result.getTotal()); // Total
        }
        Map<String, Object> response = new HashMap<>();
        response.put("labels", labels);
        response.put("data", data);
        return response;
    }

    @GetMapping("/by-day")
   public ResponseEntity<?> getPaymentsByDay() {
        List<PaymentData> results = paymentService.getPaymentsByDay();
        return ResponseEntity.ok(transformToChartData(results));
    }

    @GetMapping("/by-month")
    public ResponseEntity<?> getPaymentsByMonth() {
        List<PaymentData> results = paymentService.getPaymentsByMonth();
        return ResponseEntity.ok(transformToChartData(results));
    }

    @GetMapping("/by-year")
   public ResponseEntity<?> getPaymentsByYear() {
        List<PaymentData> results = paymentService.getPaymentsByYear();
        return ResponseEntity.ok(transformToChartData(results));
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

