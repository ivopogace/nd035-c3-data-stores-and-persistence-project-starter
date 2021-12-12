package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Future(message = "You can't schedule a meeting in the past.")
    private LocalDate date;

    @ElementCollection(targetClass = EmployeeSkill.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "schedule_activities",
            joinColumns = @JoinColumn(name = "schedule_id"))
    @Column(name = "activities")
    private Set<EmployeeSkill> activities;

    @ManyToMany
    private Set<Employee> employees;

    @ManyToMany
    private Set<Pet> pets;
}
