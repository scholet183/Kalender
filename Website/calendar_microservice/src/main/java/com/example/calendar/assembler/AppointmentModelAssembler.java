package com.example.calendar.assembler;

import com.example.calendar.controller.CalendarController;
import com.example.calendar.eto.Appointment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AppointmentModelAssembler implements RepresentationModelAssembler<Appointment, EntityModel<Appointment>> {

    @Override
    public EntityModel<Appointment> toModel(Appointment appointment) {
        return EntityModel.of(appointment,
                linkTo(methodOn(CalendarController.class).getAppointment(appointment.getId())).withSelfRel()
        );
    }
}
