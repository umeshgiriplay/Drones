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
import java.util.Optional;

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


    public boolean canBeLoaded() {
        try {
            throwExceptionIsCannotBeLoaded(null);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void throwExceptionIsCannotBeLoaded(Double weight) {

        if (getState() != DroneState.IDLE && getState() != DroneState.LOADING) {
            throw new IllegalArgumentException("Drone cannot be loaded when state is " + getState().name());
        }

        if (getBatteryCapacity() < 25) {
            throw new IllegalArgumentException("Drone cannot be loaded when battery is below 25%");
        }

        Double alreadyLoadedWeight = 0D;
        final Optional<DroneDelivery> pendingDelivery = getPendingDelivery();
        if (pendingDelivery.isPresent()) {
            alreadyLoadedWeight = pendingDelivery.get().getTotalPayloadWeight();
        }

        if(weight!=null) {
            if ((alreadyLoadedWeight + weight) > getWeightLimit()) {
                throw new IllegalArgumentException(String.format("Cannot load additional weight of %.2f gm. Drone's currently loaded payload weight is %.2f gr and the weight limit is %.2f gr.",
                        weight, alreadyLoadedWeight, getWeightLimit()));
            }
        }
        else {
            if (alreadyLoadedWeight >= getWeightLimit()) {
                throw new IllegalArgumentException(String.format("Cannot load additional weight. Drone's currently loaded payload weight is %.2f gr and the weight limit is %.2f gr.",
                        alreadyLoadedWeight, getWeightLimit()));
            }
        }
    }

    @JsonIgnore
    public DroneDelivery getOrCreatePendingDelivery() {
        return getDeliveries().stream().filter(droneDelivery -> droneDelivery.getDeliveryTime() == null).findFirst().orElseGet(() -> {
            DroneDelivery delivery = new DroneDelivery();
            delivery.setDrone(this);
            return delivery;
        });
    }

    @JsonIgnore
    public Optional<DroneDelivery> getPendingDelivery() {
        return getDeliveries().stream().filter(droneDelivery -> droneDelivery.getDeliveryTime() == null).findFirst();
    }
}
