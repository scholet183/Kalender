package com.example.calendar.service;

import com.example.calendar.eto.Appointment;

import java.util.List;

public interface CalendarService {
    Appointment addAppointment(Appointment appointment);

    List<Appointment> getAppointmentsById(Integer userId);
}
