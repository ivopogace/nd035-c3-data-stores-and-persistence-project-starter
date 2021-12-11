package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getScheduleList();
    List<Schedule> getScheduleListByPetId(Long petId);
    List<Schedule> getScheduleListByEmployeeId(Long employeeId);
    List<Schedule> getScheduleListByCustomerId(Long customerId);
    Schedule addSchedule(Schedule newSchedule);
}
