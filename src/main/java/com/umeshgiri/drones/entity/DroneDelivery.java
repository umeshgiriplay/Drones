package com.umeshgiri.drones.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drone_deliveries")
@Getter
@Setter
public class DroneDelivery extends AbstractEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    private Drone drone;

    @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL)
    private List<Payload> payloads = new ArrayList<>();

    @Column
    private LocalDateTime deliveryTime;
}
