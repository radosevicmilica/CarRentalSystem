package com.rzk.rentalservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Table(name = "rentals")
@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long vehicleId;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private String status; // ACTIVE, FINISHED

    @NotNull
    @Min(0)
    private Double basePrice;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<RentalPeriod> rentalPeriods;

    @OneToMany(mappedBy = "rental", cascade = CascadeType.ALL)
    private List<Penalty> penalties;
}
