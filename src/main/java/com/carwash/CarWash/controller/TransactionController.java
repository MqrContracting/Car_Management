package com.carwash.CarWash.controller;

import com.carwash.CarWash.Dto.TransactionDto;
import com.carwash.CarWash.Responses.ResponseModel;
import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Transaction;
import com.carwash.CarWash.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity saveTransaction(@RequestBody TransactionDto transaction) {
        System.out.println("simple test");
        try{
            return ResponseModel.success(
                    "Votre transaction à été enregistré",
                    transactionService.saveTransaction(transaction));
        }catch (Exception e){
            System.out.println("test 1234");
            //e.printStackTrace();
            return ResponseModel.error(
                    e.getMessage(),
                   HttpStatus.UNPROCESSABLE_ENTITY);
            //return ResponseEntity
                    //.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    //.body("Certaines variables sont mal remplie ou null !");
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        System.out.println("text");
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/car/{regNo}")
    public ResponseEntity<List<Transaction>> getTransactionsByCarRegNo(@PathVariable String regNo) {
        return ResponseEntity.ok(transactionService.getTransactionsByCarRegNo(regNo));
    }

    @GetMapping("/service/{serviceType}")
    public ResponseEntity<List<Transaction>> getTransactionsByServiceType(@PathVariable ServiceType serviceType) {
        return ResponseEntity.ok(transactionService.getTransactionsByServiceType(serviceType));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Transaction>> getTransactionsWithinDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsWithinDateRange(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}

