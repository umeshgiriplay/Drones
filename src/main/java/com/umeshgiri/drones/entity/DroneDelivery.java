package com.umeshgiri.drones.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "drone_deliveries")
@Getter
@Setter
public class DroneDelivery extends AbstractEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    private Drone drone;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private Set<Payload> payloads = new HashSet<>();

    @Column
    private LocalDateTime deliveryTime;

    public Double getTotalPayloadWeight() {
        return getPayloads().stream().mapToDouble(Payload::getWeight).sum();
    }
}
