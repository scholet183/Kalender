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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Testklasse für den CalendarController.
 * Nutzt Mockito zur Isolierung von Abhängigkeiten und MockMvc zur Simulation von HTTP-Anfragen.
 */
@ExtendWith(MockitoExtension.class)
public class CalendarControllerTest {
    // MockMvc ermöglicht das Testen von Web-Endpunkten, ohne den vollständigen Servlet-Container zu starten.
    private MockMvc mockMvc;
    // ObjectMapper zur Serialisierung und Deserialisierung von JSON-Daten.
    private final ObjectMapper objectMapper = new ObjectMapper();
    // Injektion des zu testenden Controllers und Mocks in diese Testklasse.
    @InjectMocks
    private CalendarController calendarController;
    // Mock für den Service, welcher die Geschäftslogik (Appointment-Verwaltung) kapselt.
    @Mock
    private CalendarService calendarService;
    // Mock für den ModelAssembler, der Domain-Objekte in HATEOAS-konforme Darstellungen umwandelt.
    @Mock
    private AppointmentModelAssembler assembler;

    /**
     * Setup-Methode, die vor jedem Test ausgeführt wird.
     * Hier wird der ObjectMapper für die HAL-Unterstützung konfiguriert
     * und die MockMvc-Instanz erstellt, welche den CalendarController isoliert testet.
     */
    @BeforeEach
    public void setup() {
        // Registrierung des Jackson2HalModule, um HAL-spezifische JSON-Darstellungen zu unterstützen.
        objectMapper.registerModule(new Jackson2HalModule());
        // Initialisierung von MockMvc mit dem zu testenden Controller.
        mockMvc = MockMvcBuilders.standaloneSetup(calendarController).build();
    }

    /**
     * Test für die Erstellung (Speicherung) eines Termins.
     * Es wird geprüft, ob der POST-Endpunkt einen neuen Termin korrekt speichert und zurückgibt.
     */
    @Test
    public void testSaveAppointment() throws Exception {
        // Erstelle ein Test-Appointment
        Appointment appointment = new Appointment();
        appointment.setId(1);
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

    /**
     * Test für das Abrufen eines bestehenden Termins über die GET-Methode.
     * Es wird sichergestellt, dass das richtige Appointment-Objekt zurückgegeben wird.
     */
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

    /**
     * Test für die Aktualisierung eines bestehenden Termins mittels PUT-Methode.
     * Der Test validiert, ob die Aktualisierung korrekt erfolgt und die geänderten Felder zurückgegeben werden.
     */
    @Test
    public void testUpdateAppointment() throws Exception {
        // Erstelle einen aktualisierten Termin
        Appointment updatedAppointment = new Appointment();
        updatedAppointment.setId(1);
        updatedAppointment.setTitle("Updated Title");
        // Setze ggf. weitere Attribute

        when(calendarService.updateAppointment(any(Appointment.class))).thenReturn(updatedAppointment);
        EntityModel<Appointment> entityModel = EntityModel.of(updatedAppointment);
        when(assembler.toModel(updatedAppointment)).thenReturn(entityModel);

        mockMvc.perform(put("/api/calendar/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAppointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedAppointment.getId()))
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    /**
     * Test für die Löschung eines bestehenden Termins mittels DELETE-Methode.
     * Hier wird geprüft, ob der Löschvorgang erfolgreich verläuft und der korrekte HTTP-Status zurückgegeben wird.
     */
    @Test
    public void testDeleteAppointment() throws Exception {
        // Simuliere, dass die Löschung erfolgreich ist
        when(calendarService.deleteAppointment(eq(1))).thenReturn(true);

        mockMvc.perform(delete("/api/calendar/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
