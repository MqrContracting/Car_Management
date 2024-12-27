package com.carwash.CarWash.service;

import com.carwash.CarWash.Dto.TransactionDto;
import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Transaction;
import com.carwash.CarWash.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final CarService carService;
    private final ServiceService serviceService;
    private final PaymentService paymentService;

    /**
     * Sauvegarde une transaction après validation des entités associées.
     *
     * @param transaction la transaction à sauvegarder
     * @return la transaction sauvegardée
     */
    public Transaction saveTransaction(TransactionDto transaction) {

        // TODO :
        /**
         * check the unicity of the car information
         * create the cars
         *
         * check the existance of the selected service in the database
         * * create the service
         *
         * check the existance of the selected payment in the database
         * * create the payment
         */

        // Valider et récupérer les entités liées
        var car = carService.getCarById(transaction.getCar())
                .orElseThrow(() -> new IllegalArgumentException("Car not found with ID: " + transaction.getCar()));

//        var service = serviceService.getServiceById(transaction.getService())
//                .orElseThrow(() -> new IllegalArgumentException("Service not found with ID: " + transaction.getService()));

        var payment = paymentService.getPaymentById(transaction.getPayment())
                .orElseThrow(() -> new IllegalArgumentException("Payment not found with ID: " + transaction.getPayment()));

//        var t = new Transaction();
//        t.setCar(car);
//        t.setService(service); // Associer les entités validées à la transaction
//        t.setPayment(payment);

        // Sauvegarder la transaction
        return null;
    }

    public List<Transaction> getTransactionsByCarRegNo(String regNo) {
        return transactionRepository.findByCar_RegNo(regNo);
    }

    public List<Transaction> getTransactionsByServiceType(ServiceType serviceType) {
        return transactionRepository.findByService_ServiceType(serviceType);
    }

    public List<Transaction> getTransactionsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findTransactionsWithinDateRange(startDate, endDate);
    }

    public List<Transaction> getAllTransactions() {

        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
