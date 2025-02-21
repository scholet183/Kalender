package com.example.calendar.service.impl;

import com.example.calendar.eto.Appointment;
import com.example.calendar.repository.CalendarRepository;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService {

    @Autowired
    CalendarRepository calendarRepository;

    @Override
    public Appointment addAppointment(Appointment appointment) {
        return calendarRepository.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsById(Integer userId) {
        return calendarRepository.findByUserId(userId);
    }
}
