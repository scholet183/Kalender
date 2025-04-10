package com.example.calendar.service.impl;

import com.example.calendar.eto.Appointment;
import com.example.calendar.repository.CalendarRepository;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public Appointment getAppointmentById(Integer id) {
        return calendarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid appointment id" + id));
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        // Prüfen, ob der Termin existiert (die ID darf nicht null sein)
        if (appointment.getId() == null || calendarRepository.findById(appointment.getId()).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment not found with id: " + appointment.getId());
        }
        // Der save()-Aufruf aktualisiert den Termin, wenn die ID bereits vorhanden ist
        return calendarRepository.save(appointment);
    }

    @Override
    public boolean deleteAppointment(Integer id) {
        Appointment appointment = calendarRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid appointment id" + id));
        calendarRepository.delete(appointment);
        return true;
    }
}
