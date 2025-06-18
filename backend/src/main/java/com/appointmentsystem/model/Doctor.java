package com.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Link to the user account

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "consultation_duration")
    private int consultationDuration = 30; // Default to 30 minutes

    @Column(name = "employment_type")
    private String employmentType; // Full-time, Part-time, etc.
}