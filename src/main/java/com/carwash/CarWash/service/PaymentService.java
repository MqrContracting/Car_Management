package com.carwash.CarWash.service;

import com.carwash.CarWash.entity.Car;
import com.carwash.CarWash.entity.Client;
import com.carwash.CarWash.entity.Enum.PaymentStatus;
import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Payment;
import com.carwash.CarWash.entity.Transaction;
import com.carwash.CarWash.repository.CarRepository;
import com.carwash.CarWash.repository.ClientRepository;
import com.carwash.CarWash.repository.PaymentRepository;
import com.carwash.CarWash.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;

    private final ServiceRepository serviceRepository;
    private final CarRepository carRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;


     public Payment savePayment(Payment payment) {
    // Validate client details
    Client client = payment.getClient();
    if (client == null || client.getPhoneNumber() == null) {
        throw new IllegalArgumentException("Client and client phone number are required.");
    }

    // Retrieve or create the client
    if (client.getId() == null) {
        Client finalClient = client;
        client = clientService.findClientByPhoneNumber(client.getPhoneNumber())
                .orElseGet(() -> clientService.saveClient(finalClient));
        System.out.println("CLIENT SAVED/RETRIEVED: " + client);
    } else {
        Client finalClient2 = client;
        client = clientRepository.findById(client.getId())
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + finalClient2.getId()));
    }

    // Ensure client cars collection is initialized
    if (client.getCars() == null) {
        client.setCars(new ArrayList<>());
    }

    // Retrieve or create the car
    Car car = payment.getCar();
    if (car == null || car.getRegNo() == null) {
        throw new IllegalArgumentException("Car and car registration number are required.");
    }

    if (car.getId() == null) {
        Car finalCar = car;
        Client finalClient1 = client;
        car = carRepository.findByRegNo(car.getRegNo())
                .orElseGet(() -> {
                    finalCar.setClient(finalClient1);
                    return carRepository.save(finalCar);
                });
        client.getCars().add(car);
        System.out.println("CAR SAVED/RETRIEVED: " + car);
    } else {
        Car finalCar1 = car;
        car = carRepository.findById(car.getId())
                .orElseThrow(() -> new RuntimeException("Car not found with ID: " + finalCar1.getId()));
    }

    // Retrieve the service
    if (payment.getService() == null || payment.getService().getId() == null) {
        throw new IllegalArgumentException("Service ID is required.");
    }

    Long serviceId = payment.getService().getId();
    com.carwash.CarWash.entity.Service service = serviceRepository.findById(serviceId)
            .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

    // Create and save the payment
    Payment newPayment = new Payment();
    newPayment.setClient(client);
    newPayment.setCar(car);
    newPayment.setService(service);
    newPayment.setPaymentType(payment.getPaymentType());
    newPayment.setGivenPrice(payment.getGivenPrice());
    newPayment.setStatus(payment.getStatus());
    newPayment.setAdditionalDetails(payment.getAdditionalDetails());
    newPayment.setPaymentDate(LocalDateTime.now());
    newPayment.setIsPaidNow(payment.getIsPaidNow());

    Payment result = paymentRepository.save(newPayment);

    // Update client's payments list
    if (client.getPayments() == null) {
        client.setPayments(new ArrayList<>());
    }
    client.getPayments().add(result);

    System.out.println("PAYMENT SAVED: " + newPayment);
    return result;
}








//    public Payment updatePayment(Payment payment) {
////        if (payment == null || payment.getId() == null) {
////            throw new IllegalArgumentException("Payment or Payment ID must not be null");
////        }
////
////        // Ensure the payment exists before updating
////        Optional<Payment> existingPayment = paymentRepository.findById(payment.getId());
////        if (existingPayment.isEmpty()) {
////            throw new IllegalStateException("Payment with ID " + payment.getId() + " does not exist");
////        }
//
//        return paymentRepository.save(payment);
//    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getAllPayments() {
        //System.out.println("Le resultat de payment est: "+result);
        return paymentRepository.findAllWithDetails();
    }

    @Transactional
    public Payment updatePaymentStatus(Long id, String status) {
        // Récupérer le paiement ou lever une exception
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + id));

        // Valider le statut
        if (!isValidStatusTransition(payment.getStatus(), status)) {
            throw new IllegalArgumentException("Invalid status transition: "
                    + payment.getStatus() + " to " + status);
        }

        // Mettre à jour le statut et sauvegarder
        payment.setStatus(PaymentStatus.valueOf(status));
        //System.out.println("Le statut du paiement est: "+payment.getStatus());
        return paymentRepository.save(payment);
    }

    private boolean isValidStatusTransition(PaymentStatus currentStatus, String newStatus) {
        // Valider la transition de statut : PENDING → COMPLETED uniquement
        return currentStatus == PaymentStatus.PENDING && "COMPLETED".equals(newStatus);
    }

    public List<Payment> getPaymentsByCar_RegNo(String regNo) {
        return paymentRepository.findByCar_RegNo(regNo);
  }

    //public List<Payment> getPaymentsByService_ServiceType(ServiceType serviceType) {
    //    return paymentRepository.findPaymentsByService_ServiceType(serviceType);
   // }

    public List<Transaction> getPaymentsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsWithinDateRange(startDate, endDate);
    }

    public List<Payment> getPaymentsByClient(Long clientId) {
        return paymentRepository.findByClientId(clientId);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}

