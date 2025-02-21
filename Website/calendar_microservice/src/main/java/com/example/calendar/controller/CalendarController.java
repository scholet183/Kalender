package com.example.calendar.controller;

import com.example.calendar.eto.Appointment;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @PostMapping("/addAppointment")
    public Appointment addAppointment(@RequestBody Appointment appointment) {
        return calendarService.addAppointment(appointment);
    }

    @GetMapping("/getAppointments")
    public List<Appointment> getAppointments(@RequestParam Integer userId) {
        return calendarService.getAppointmentsById(userId);
    }


}
