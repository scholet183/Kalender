package com.example.calendar.service.impl;

import com.example.calendar.eto.Appointment;
import com.example.calendar.repository.CalendarRepository;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Appointment getAppointmentById(Integer id) {

        Appointment appointment = calendarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid appointment id" + id));
        return appointment;
    }
}
