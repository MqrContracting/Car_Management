package com.carwash.CarWash.dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PaymentData {
    private LocalDateTime paymentDate;
    private Double total;
    private LocalDateTime period;

    // Constructeurs, getters et setters
    public PaymentData(LocalDateTime date, Double total) {
        this.paymentDate = date;
        this.total = total;
        this.period = LocalDateTime.now();

    }


}
