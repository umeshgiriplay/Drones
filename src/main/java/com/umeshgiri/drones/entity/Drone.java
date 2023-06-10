package com.umeshgiri.drones.entity;

import com.umeshgiri.drones.enums.DroneModel;
import com.umeshgiri.drones.enums.DroneState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drones")
@Getter
@Setter
public class Drone extends AbstractEntity{

    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneModel model;

    /**
     * The weight limit of the drone in grams.
     */
    @DecimalMax(value = "500")
    @Column(nullable = false, length = 3)
    private double weightLimit;

    @DecimalMax(value = "100")
    @Column(nullable = false, length = 3)
    private double batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneState state;

    @OneToMany(mappedBy = "drone")
    private List<DroneDelivery> deliveries = new ArrayList<>();
}
