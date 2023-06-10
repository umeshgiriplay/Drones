package com.umeshgiri.drones.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umeshgiri.drones.enums.PayloadType;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name = "payloads")
@Getter
@Setter
public class Payload extends AbstractEntity {

    @Pattern(regexp = "^[a-zA-Z0-9_-]+$",message = "should only contain letters, numbers, ‘-‘, ‘_’")
    @Column(nullable = false)
    @NotNull
    @NotEmpty
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private PayloadType payloadType;

    /**
     * The weight in grams.
     */
    @Column(nullable = false)
    @NotNull
    @DecimalMin(value = "0")
    private Double weight;

    @Pattern(regexp = "^[A-Z0-9_]+$",message = "should only contain upper case letters, underscore and numbers")
    @Column(nullable = false)
    @NotNull
    private String code;

    @OneToOne
    private Document image;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    @JsonIgnore
    private DroneDelivery delivery;

    @Transient
    private MultipartFile uploadImage;
}