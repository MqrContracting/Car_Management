package com.carwash.CarWash.entity;

import com.carwash.CarWash.entity.Enum.PaymentStatus;
import com.carwash.CarWash.entity.Enum.PaymentType;
import com.carwash.CarWash.entity.Enum.isPaid;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("payments")
    //@JsonManagedReference

    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties("clients")
    //@JsonManagedReference
    //@JsonBackReference
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

     //Remplacement de la relation Service par une collection de services
    @ManyToOne(fetch = FetchType.EAGER)
    private Service service;

    private Double givenPrice;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    @JsonIgnoreProperties("isPaid")
    private isPaid isPaidNow;

    private String additionalDetails;

    private LocalDateTime paymentDate;

    // Nouveau champ pour stocker le prix total des services
}
