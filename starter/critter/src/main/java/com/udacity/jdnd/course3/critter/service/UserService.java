package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.DTO.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;

import javax.validation.constraints.Pattern;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<Customer> getCustomersList();
    List<Employee> getEmployeeByAvailability(EmployeeRequestDTO employeeDTO);
    Customer getCustomerByPetId(Long petId);
    Employee getEmployee(Long employeeId);
    Customer addCustomer(Customer newCustomer);
    Employee addEmployee(Employee newEmployee);
    void addAvailabilityByEmployeeId(Set<DayOfWeek> daysAvailable, Long employeeId);
    Customer getCustomerById(Long ownerId);
    boolean customerExists(String name, @Pattern(regexp = "(^$|[0-9]{10})", message = "Please enter a valid 10 Digit Phone Number") String phoneNumber);
    boolean employeeExists(String name, Set<EmployeeSkill> skills);
}
