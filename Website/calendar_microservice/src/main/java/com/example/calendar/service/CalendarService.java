package com.example.calendar.service;

import com.example.calendar.eto.Appointment;

import java.util.List;

public interface CalendarService {
    Appointment addAppointment(Appointment appointment);

    List<Appointment> getAppointmentsById(Integer userId);

    Appointment getAppointmentById(Integer id);

    Appointment updateAppointment(Appointment appointment);

    boolean deleteAppointment(Integer id);
}
