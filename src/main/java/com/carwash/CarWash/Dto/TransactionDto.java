package com.carwash.CarWash.Dto;

import com.carwash.CarWash.entity.Car;
import com.carwash.CarWash.entity.Payment;
import com.carwash.CarWash.entity.Service;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @NotNull()
    private long car;

    @NotNull()
    private long service;

    @NotNull()
    private long payment;

    @NotNull()
    private LocalDateTime transactionDate;
}
