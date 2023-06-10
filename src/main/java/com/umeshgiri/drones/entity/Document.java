package com.umeshgiri.drones.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Document extends AbstractEntity {
    @JsonIgnore
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @JsonIgnore
    private String extension;

    @Column(nullable = false)
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @Column(nullable = false)
    private String url;
}
