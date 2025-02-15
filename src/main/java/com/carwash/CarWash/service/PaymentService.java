package com.carwash.CarWash.service;

import com.carwash.CarWash.dtos.PaymentData;
import com.carwash.CarWash.entity.Car;
import com.carwash.CarWash.entity.Client;
import com.carwash.CarWash.entity.Enum.PaymentStatus;
import com.carwash.CarWash.entity.Enum.PaymentType;
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


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;

    private final ServiceRepository serviceRepository;
    private final CarRepository carRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    //Ajouter un paiement
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


    // Total des paiements
    public double calculateTotalIncome() {
        return paymentRepository.findAll()
                .stream()
                .mapToDouble(Payment::getGivenPrice)
                .sum();

    }

    // Total des paiements en cash
    public double getCashPayments() {
        return paymentRepository.sumGivenPriceByPaymentMethod(PaymentType.CASH);
    }

    // Total des paiements via Juice
    public double getJuicePayments() {
        return paymentRepository.sumGivenPriceByPaymentMethod(PaymentType.JUICE);
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    //Tous les paiements
    public List<Payment> getAllPayments() {
        //System.out.println("Le resultat de payment est : "+result);
        return paymentRepository.findAllWithDetails();
    }

    // Paiements par jour
    public List<PaymentData> getPaymentsByDay() {
    List<Object[]> results = paymentRepository.getTotalPaymentsByDay();
    return results.stream()
            .map(result -> {
                // Convertir java.sql.Date en LocalDateTime
                java.sql.Date sqlDate = (java.sql.Date) result[0];
                LocalDateTime paymentDate = sqlDate.toLocalDate().atStartOfDay(); // Début du jour
                Double amount = (Double) result[1];
                // Créer un nouvel objet PaymentData avec une période correcte
                return new PaymentData(paymentDate, amount, paymentDate); // Période = jour
            })
            .collect(Collectors.toList());
}





    // Paiements par mois
   public List<PaymentData> getPaymentsByMonth() {
    List<Object[]> results = paymentRepository.getTotalPaymentsByMonth();
    return results.stream()
        .map(result -> {
            Object dateObject = result[0];
            LocalDateTime date = null;

            // Vérifier et convertir l'objet dateObject
            if (dateObject instanceof Integer) {
                // Si c'est un mois (Integer), utilisez une année par défaut (ex. : 2023)
                int month = (Integer) dateObject;
                date = LocalDateTime.of(2024, month, 1, 0, 0, 0, 0);
            } else if (dateObject instanceof java.sql.Date) {
                // Si le regroupement est par mois, convertissez java.sql.Date
                date = ((java.sql.Date) dateObject).toLocalDate().atStartOfDay().withDayOfMonth(1);
            } else if (dateObject instanceof LocalDateTime) {
                // Si c'est déjà un LocalDateTime, ajustez pour le premier jour du mois
                date = ((LocalDateTime) dateObject).withDayOfMonth(1);
            }

            // Récupérer le montant associé
            Double amount = (Double) result[1];

            // Créer un objet PaymentData avec une période correcte
            return new PaymentData(date, amount, date);
        })
        .collect(Collectors.toList());
}




    // Paiements par année
    public List<PaymentData> getPaymentsByYear() {
    List<Object[]> results = paymentRepository.getTotalPaymentsByYear();
    return results.stream()
            .map(result -> {
                LocalDate date = null;
                Object dateObject = result[0];
                if (dateObject instanceof Integer) {
                    // Si la période est une année en Integer
                    date = LocalDate.of((Integer) dateObject, 1, 1); // 1er janvier de l'année
                } else if (dateObject instanceof java.sql.Date) {
                    date = ((java.sql.Date) dateObject).toLocalDate().withDayOfYear(1); // Premier jour de l'année
                }
                Double amount = (Double) result[1];
                // Créer un nouvel objet PaymentData avec une période correcte
                assert date != null;
                return new PaymentData(date.atStartOfDay(), amount, date.atStartOfDay());
            })
            .collect(Collectors.toList());
}






    //Mettre à jour un paiement (le status d'un paiement)
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
        //System.out.println("Le statut du paiement est : "+payment.getStatus());
        return paymentRepository.save(payment);
    }

    private boolean isValidStatusTransition(PaymentStatus currentStatus, String newStatus) {
        // Valider la transition de statut : PENDING → COMPLETED uniquement
        return currentStatus == PaymentStatus.PENDING && "COMPLETED".equals(newStatus);
    }

    public List<Payment> getPaymentsByCar_RegNo(String regNo) {
        return paymentRepository.findByCar_RegNo(regNo);
  }

    public List<Transaction> getPaymentsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findPaymentsWithinDateRange(startDate, endDate);
    }

    public List<Payment> getPaymentsByClient(Long clientId) {
        return paymentRepository.findByClientId(clientId);
    }

    //Supprimer un paiement, on ne sait jamais
    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}

