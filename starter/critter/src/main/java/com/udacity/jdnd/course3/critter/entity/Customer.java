package com.udacity.jdnd.course3.critter.entity;

import lombok.*;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Nationalized
    private String name;
    @Pattern(regexp="(^$|[0-9]{10})", message = "Please enter a valid 10 Digit Phone Number")
    private String phoneNumber;
    @Column(columnDefinition = "text")
    private String notes;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Pet> pets;
}
