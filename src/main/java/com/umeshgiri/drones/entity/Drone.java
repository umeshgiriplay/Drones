package com.umeshgiri.drones.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umeshgiri.drones.enums.DroneModel;
import com.umeshgiri.drones.enums.DroneState;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drones")
@Getter
@Setter
public class Drone extends AbstractEntity {

    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    @NotNull
    @NotEmpty
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DroneModel model;

    /**
     * The weight limit of the drone in grams.
     */
    @DecimalMax(value = "500")
    @DecimalMin(value = "0")
    @Column(nullable = false)
    @NotNull
    private Double weightLimit;

    @DecimalMax(value = "100")
    @DecimalMin(value = "0")
    @Column(nullable = false)
    @NotNull
    private Double batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private DroneState state;

    @OneToMany(mappedBy = "drone")
    @JsonIgnore
    private List<DroneDelivery> deliveries = new ArrayList<>();
}
