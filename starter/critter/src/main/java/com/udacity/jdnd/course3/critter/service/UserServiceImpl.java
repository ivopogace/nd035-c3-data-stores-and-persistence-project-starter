package com.udacity.jdnd.course3.critter.service;

import com.google.common.collect.Lists;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.DTO.EmployeeRequestDTO;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.util.*;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public UserServiceImpl(EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getCustomersList() {
        return customerRepository.findAll();
    }

    @Override
    public List<Employee> getEmployeeByAvailability(EmployeeRequestDTO employeeDTO) {
        Set<Employee> availableEmployee = employeeRepository.findAllByDaysAvailableContaining(employeeDTO.getDate().getDayOfWeek());
        List<Employee> availableEmployeeWithSkills = new ArrayList<>();
        availableEmployee.forEach(employee -> {
            if (employee.getSkills().containsAll(employeeDTO.getSkills())){
                availableEmployeeWithSkills.add(employee);
            }
        });
        return availableEmployeeWithSkills;
    }

    @Override
    public Customer getCustomerByPetId(Long petId) {
        return customerRepository.findByPetsId(petId);
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);
        if (employee.isPresent())
            return employee.get();
        else throw new EntityNotFoundException("Employee not found id: " + employeeId);
    }

    @Override
    public Customer addCustomer(Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }

    @Override
    public Employee addEmployee(Employee newEmployee) {
        return employeeRepository.save(newEmployee);
    }

    @Override
    public void addAvailabilityByEmployeeId(Set<DayOfWeek> daysAvailable, Long employeeId) {
        Optional<Employee> existingEmployee = employeeRepository.findById(employeeId);
        if (existingEmployee.isPresent()){
            Employee employee = existingEmployee.get();
            employee.setDaysAvailable(daysAvailable);
            employeeRepository.save(employee);
        }
    }

    @Override
    public Customer getCustomerById(Long ownerId) {
        Optional<Customer> customer = customerRepository.findById(ownerId);
        if (customer.isPresent())
            return customer.get();
        else throw new EntityNotFoundException("Customer(Owner) not found id: " + ownerId);
    }

    @Override
    public boolean customerExists(String name, String phoneNumber) {
        return customerRepository.existsByNameAndPhoneNumber(name, phoneNumber);
    }

    @Override
    public boolean employeeExists(String name, Set<EmployeeSkill> skills) {
        boolean result = false;
        List<Employee> existingEmployee = employeeRepository.findAllByName(name);
        if (!existingEmployee.isEmpty()){
            result = existingEmployee.stream().anyMatch(employee -> new ArrayList<>(employee.getSkills()).equals((Lists.newArrayList(skills))));
        }
        return result;
    }
}
