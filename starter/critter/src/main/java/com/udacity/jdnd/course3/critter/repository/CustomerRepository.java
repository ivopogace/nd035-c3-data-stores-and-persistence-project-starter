package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Pattern;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByPetsId(Long petsId);
    boolean existsByNameAndPhoneNumber(String name, @Pattern(regexp = "(^$|[0-9]{10})", message = "Please enter a valid 10 Digit Phone Number") String phoneNumber);
}
