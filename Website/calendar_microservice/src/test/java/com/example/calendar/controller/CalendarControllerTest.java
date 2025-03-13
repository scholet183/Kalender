package com.example.calendar.controller;

import com.example.calendar.assembler.AppointmentModelAssembler;
import com.example.calendar.eto.Appointment;
import com.example.calendar.service.CalendarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private CalendarController calendarController;

    @Mock
    private CalendarService calendarService;

    @Mock
    private AppointmentModelAssembler assembler;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new Jackson2HalModule());
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
    }

    @Test
    public void testSaveAppointment() throws Exception {
        // Erstelle ein Test-Appointment
        Appointment appointment = new Appointment();
        appointment.setId(1);
        // Falls dein Appointment weitere Attribute wie userId hat, kannst du diese hier setzen.

        // Simuliere das Speichern und die Assemblierung
        when(calendarService.addAppointment(any(Appointment.class))).thenReturn(appointment);
        EntityModel<Appointment> entityModel = EntityModel.of(appointment);
        when(assembler.toModel(appointment)).thenReturn(entityModel);

        mockMvc.perform(post("/api/calendar/addAppointment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(appointment.getId()));
    }

    @Test
    public void testGetAppointment() throws Exception {
        int appointmentId = 1;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);

        when(calendarService.getAppointmentById(appointmentId)).thenReturn(appointment);
        EntityModel<Appointment> entityModel = EntityModel.of(appointment);
        when(assembler.toModel(appointment)).thenReturn(entityModel);

        mockMvc.perform(get("/api/calendar/{id}", appointmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointmentId));
    }
}
