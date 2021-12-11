package com.udacity.jdnd.course3.critter.controller;

import com.google.common.collect.Sets;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.DTO.ScheduleDTO;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final PetService petService;
    private final ScheduleService scheduleService;

    public ScheduleController(ModelMapper modelMapper, UserService userService, PetService petService, ScheduleService scheduleService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.petService = petService;
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        return convertToScheduleDTO(scheduleService.addSchedule(convertToScheduleEntity(scheduleDTO)));
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> scheduleList = scheduleService.getScheduleList();
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByPetId(petId);
        if (scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for pet id: " + petId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByEmployeeId(employeeId);
        if (scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for employee id: " + employeeId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> scheduleList = scheduleService.getScheduleListByCustomerId(customerId);
        if (scheduleList.isEmpty())
            throw new EntityNotFoundException("Schedule not found for customer id: " + customerId);
        List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
        scheduleList.forEach(schedule -> scheduleDTOList.add(convertToScheduleDTO(schedule)));
        return scheduleDTOList;
    }

    private Schedule convertToScheduleEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);
        schedule.setDate(scheduleDTO.getDate());
        schedule.setActivities(scheduleDTO.getActivities());

        List<Employee> employeeList = new ArrayList<>();
        if (scheduleDTO.getEmployeeIds() != null){
        scheduleDTO.getEmployeeIds().forEach(id -> employeeList.add(userService.getEmployee(id)));}
        Set<Employee> employeeSet = Sets.newHashSet(employeeList);
        schedule.setEmployees(employeeSet);

        List<Pet> petList = new ArrayList<>();
        if (scheduleDTO.getPetIds() != null){
        scheduleDTO.getPetIds().forEach(id -> petList.add(petService.getPetById(id)));}
        Set<Pet> petSet = Sets.newHashSet(petList);
        schedule.setPets(petSet);
        return schedule;
    }

    private ScheduleDTO convertToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = modelMapper.map(schedule, ScheduleDTO.class);
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setDate(schedule.getDate());
        List<Long> employeeIds = new ArrayList<>();
        List<Long> petIds = new ArrayList<>();
        schedule.getEmployees().forEach(employee -> employeeIds.add(employee.getId()));
        schedule.getPets().forEach(pet -> petIds.add(pet.getId()));
        scheduleDTO.setEmployeeIds(employeeIds);
        scheduleDTO.setPetIds(petIds);
        return scheduleDTO;
    }
}
