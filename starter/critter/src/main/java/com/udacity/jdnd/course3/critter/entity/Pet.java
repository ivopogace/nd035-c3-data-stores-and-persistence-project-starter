package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Entity
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private PetType type;
    @Nationalized
    private String name;
    @Past(message = "Date should be in the past.")
    private LocalDate birthDate;
    @Column(columnDefinition = "text")
    private String notes;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
