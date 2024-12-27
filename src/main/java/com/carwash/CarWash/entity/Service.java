package com.carwash.CarWash.entity;

import com.carwash.CarWash.entity.Enum.ServiceType;
import com.carwash.CarWash.entity.Enum.Valeting;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceType serviceType;// Enum (NORMAL_WASH, FOAM_WASH)

    @Enumerated(EnumType.STRING)
    @Null
    @Column
    private Valeting valeting;

    @Column(nullable = false)
    private Double price;
}
