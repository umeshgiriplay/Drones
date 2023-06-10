package com.umeshgiri.drones.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "drone_battery_audits")
@Getter
@Setter
public class DroneBatteryAudit extends AbstractEntity {

    @ManyToOne(optional = false)
    private Drone drone;

    @Column(nullable = false)
    private Double batteryCapacity;
}
