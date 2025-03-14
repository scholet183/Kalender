package com.example.calendar.controller;

import com.example.calendar.assembler.AppointmentModelAssembler;
import com.example.calendar.eto.Appointment;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private AppointmentModelAssembler assembler;

    @PostMapping("/addAppointment")
    public ResponseEntity<EntityModel<Appointment>> saveAppointment(@RequestBody Appointment appointment) {
        Appointment createdAppointment = calendarService.addAppointment(appointment);
        EntityModel<Appointment> appointmentModel = assembler.toModel(createdAppointment);
        return ResponseEntity
                .created(linkTo(methodOn(CalendarController.class).getAppointment(createdAppointment.getId())).toUri())
                .body(appointmentModel);
    }

    @GetMapping("/getAppointments")
    public ResponseEntity<CollectionModel<EntityModel<Appointment>>> getAllAppointments(@RequestParam Integer userId) {
        List<EntityModel<Appointment>> appointments = calendarService.getAppointmentsById(userId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Appointment>> collectionModel = CollectionModel.of(appointments,
                linkTo(methodOn(CalendarController.class).getAllAppointments(userId)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Appointment>> getAppointment(@PathVariable Integer id) {
        Appointment appointment = calendarService.getAppointmentById(id);
        return ResponseEntity.ok(assembler.toModel(appointment));
    }
}
