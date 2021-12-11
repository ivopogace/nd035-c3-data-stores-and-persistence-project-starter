package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> getScheduleList() {
        return scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> getScheduleListByPetId(Long petId) {
        return scheduleRepository.findAllByPetsId(petId);
    }

    @Override
    public List<Schedule> getScheduleListByEmployeeId(Long employeeId) {
        return scheduleRepository.findAllByEmployeesId(employeeId);
    }

    @Override
    public List<Schedule> getScheduleListByCustomerId(Long customerId) {
        return scheduleRepository.findAllByPetsCustomerId(customerId);
    }

    @Override
    public Schedule addSchedule(Schedule newSchedule) {
        return scheduleRepository.save(newSchedule);
    }
}
