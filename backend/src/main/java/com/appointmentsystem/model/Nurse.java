package com.appointmentsystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "nurses")
public class Nurse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reg_number", nullable = false, unique = true)
    private String regNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "is_active")
    private boolean isActive = true;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user; // Link to the user account
}
