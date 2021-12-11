package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.UserService;
import com.udacity.jdnd.course3.critter.DTO.CustomerDTO;
import com.udacity.jdnd.course3.critter.DTO.EmployeeDTO;
import com.udacity.jdnd.course3.critter.DTO.EmployeeRequestDTO;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final PetService petService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, PetService petService, ModelMapper modelMapper) {
        this.userService = userService;
        this.petService = petService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            if (userService.customerExists(customerDTO.getName(), customerDTO.getPhoneNumber()))
                throw new EntityExistsException(String
                        .format("Customer with name %s and phone number %s already exists",
                        customerDTO.getName(),
                        customerDTO.getPhoneNumber()));
            return convertToCustomerDto(userService.addCustomer(convertToCustomerEntity(customerDTO)));
        } catch (Exception ex){
            throw new ValidationException(ex);
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customerList = userService.getCustomersList();
        List<CustomerDTO> customerDTOList = new ArrayList<>();
        customerList.forEach(customer -> customerDTOList.add(convertToCustomerDto(customer)));
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        try {
            return convertToCustomerDto(userService.getCustomerByPetId(petId));
        }catch (Exception ex){
            throw new EntityNotFoundException("Customer is not found with petId: " + petId);
        }

    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        if (userService.employeeExists(employeeDTO.getName(), employeeDTO.getSkills()))
            throw new EntityExistsException(String
                    .format("Emplyee with name %s and skills %s already exists",
                            employeeDTO.getName(),
                            employeeDTO.getSkills()));
        return convertToEmployeeDto(userService.addEmployee(convertToEmployeeEntity(employeeDTO)));
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            return convertToEmployeeDto(userService.getEmployee(employeeId));
        } catch (Exception ex){
            throw new EntityNotFoundException("Employee is not found with employeeId: " + employeeId);
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable Long employeeId) {
        userService.addAvailabilityByEmployeeId(daysAvailable,employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();
        try {
            List<Employee> employeeList = userService.getEmployeeByAvailability(employeeDTO);
            employeeList.forEach(employee -> employeeDTOList.add(convertToEmployeeDto(employee)));
        } catch (Exception ex){
            throw new EntityNotFoundException("Something went wrong generating the employee availability list!");
        }
        return employeeDTOList;
    }


    private Customer convertToCustomerEntity(CustomerDTO customerDTO) {
        ArrayList<Pet> pets = new ArrayList<>();

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
        if (customerDTO.getPetIds() != null) {
            customerDTO.getPetIds().forEach(petId -> {
                Pet pet = petService.getPetById(petId);
                pets.add(pet);
                customer.setPets(pets);
            });
        } else {
            customer.setPets(petService.getPetsListByOwnerId(customerDTO.getId()));
        }
        return customer;
    }

    private CustomerDTO convertToCustomerDto(Customer customer) {
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        List<Long> petIds = new ArrayList<>();
        if (!customer.getPets().isEmpty()) {
            List<Long> finalPetIds = petIds;
            customer.getPets().forEach(pet -> finalPetIds.add(pet.getId()));
            customerDTO.setPetIds(petIds);
        }else {
            petIds = petService.getPetsListByOwnerId(customer.getId()).stream()
                    .map(Pet::getId)
                    .collect(Collectors.toList());
            customerDTO.setPetIds(petIds);
        }
        return customerDTO;
    }

    private Employee convertToEmployeeEntity(EmployeeDTO employeeDTO) {
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return employee;
    }

    private EmployeeDTO convertToEmployeeDto(Employee employee) {
        EmployeeDTO employeeDTO = modelMapper.map(employee, EmployeeDTO.class);
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setSkills(employee.getSkills());
        employeeDTO.setDaysAvailable(employee.getDaysAvailable());
        return employeeDTO;
    }
}
