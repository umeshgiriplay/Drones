package com.umeshgiri.drones.entity;

import com.umeshgiri.drones.enums.PayloadType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Entity
@Table(name = "payloads")
@Getter
@Setter
public class Payload extends AbstractEntity {

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayloadType payloadType;

    /**
     * The weight in grams.
     */
    @Column(nullable = false)
    private double weight;

    @Pattern(regexp = "^[A-Z0-9_]+$")
    @Column(nullable = false)
    private String code;

    @OneToOne(optional = false)
    private Document image;

    @ManyToOne
    @JoinColumn(nullable = false)
    private DroneDelivery delivery;
}