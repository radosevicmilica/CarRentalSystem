package com.rzk.vehicleservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "vehicle_images", schema = "vehicle_db")
public class VehicleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ColumnDefault("0")
    @Column(name = "is_main", nullable = false)
    private Boolean isMain;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;


}