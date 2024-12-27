package com.carwash.CarWash.controller;


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

//    @GetMapping("/service/{serviceType}")
//    public ResponseEntity<List<Payment>> getPaymentsByByService_ServiceType(ServiceType serviceType) {
//        return ResponseEntity.ok(paymentService.getPaymentsByService_ServiceType(serviceType));
//    }
    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getPaymentsWithinDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(paymentService.getPaymentsWithinDateRange(startDate, endDate));
    }


    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Payment>> getPaymentsByClient(@PathVariable Long clientId) {
        List<Payment> payments = paymentService.getPaymentsByClient(clientId);
        return ResponseEntity.ok(payments);
    }
//
//    @PutMapping("/api/payments/{id}")
//    public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
//        return ResponseEntity.ok(paymentService.updatePayment(payment));
//    }

//    @PatchMapping("/api/payments/{id}/status")
//    public ResponseEntity<Payment> updatePaymentStatus(@PathVariable Long id, @RequestBody Map<String, String> updates) {
//    String status = updates.get("status");
//
//    // Vérifier si le statut est vide ou null
//    if (status == null || status.isEmpty()) {
//        return ResponseEntity.badRequest().body(null);
//    }
//
//    // Vérifier si le statut fourni est valide
//    try {
//        PaymentStatus newStatus = PaymentStatus.valueOf(status);
//
//        Optional<Payment> paymentOptional = paymentService.getPaymentById(id);
//
//        if (paymentOptional.isPresent()) {
//            Payment payment = paymentOptional.get();
//            payment.setStatus(newStatus);
//
//            // Mettre à jour le paiement
//            Payment updatedPayment = paymentService.updatePayment(payment);
//            System.out.println(status);
//            return ResponseEntity.ok(updatedPayment);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.badRequest().build(); // Statut invalide
//    }
//}
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

