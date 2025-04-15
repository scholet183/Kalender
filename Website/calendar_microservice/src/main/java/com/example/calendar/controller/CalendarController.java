package com.example.calendar.controller;

import com.example.calendar.assembler.AppointmentModelAssembler;
import com.example.calendar.eto.Appointment;
import com.example.calendar.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * REST-Controller für den Kalender.
 * Dieser Controller stellt Endpunkte bereit zum Erstellen, Abrufen, Aktualisieren und Löschen von Terminen.
 */
@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

    /**
     * Kalender-Service zur Verwaltung der Terminlogik.
     * Wird per Autowired-Injektion eingebunden.
     */
    @Autowired
    private CalendarService calendarService;

    /**
     * Assembler zur Umwandlung von Appointment-Objekten in HATEOAS-konforme EntityModel-Darstellungen.
     * Durch Dependency Injection wird er automatisch bereitgestellt.
     */
    @Autowired
    private AppointmentModelAssembler assembler;

    /**
     * POST-Endpunkt zum Speichern eines neuen Termins.
     * Bei erfolgreicher Erstellung wird ein HATEOAS-konformes EntityModel zurückgegeben und der Location-Header gesetzt.
     *
     * @param appointment Das übergebene Terminobjekt, das im RequestBody im JSON-Format gesendet wird.
     * @return ResponseEntity mit dem erstellten Termin (inklusive Hypermedia-Links) und HTTP-Status 201 Created.
     */
    @PostMapping("/addAppointment")
    public ResponseEntity<EntityModel<Appointment>> saveAppointment(@RequestBody Appointment appointment) {
        Appointment createdAppointment = calendarService.addAppointment(appointment);
        EntityModel<Appointment> appointmentModel = assembler.toModel(createdAppointment);
        return ResponseEntity
                .created(linkTo(methodOn(CalendarController.class).getAppointment(createdAppointment.getId())).toUri())
                .body(appointmentModel);
    }

    /**
     * GET-Endpunkt zum Abrufen aller Termine eines bestimmten Benutzers.
     * Es werden alle Termindaten als CollectionModel zurückgegeben, welche außerdem einen Self-Link enthält.
     *
     * @param userId Die ID des Benutzers, dessen Termine abgerufen werden sollen (als Request-Parameter).
     * @return ResponseEntity mit einer Sammlung von HATEOAS-konformen EntityModels der Termine und HTTP-Status 200 OK.
     */
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

    /**
     * GET-Endpunkt zum Abrufen eines einzelnen Termins anhand seiner ID.
     *
     * @param id Die eindeutige ID des gewünschten Termins (als Pfadvariable).
     * @return ResponseEntity mit dem HATEOAS-konformen EntityModel des Termins und HTTP-Status 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Appointment>> getAppointment(@PathVariable Integer id) {
        Appointment appointment = calendarService.getAppointmentById(id);
        return ResponseEntity.ok(assembler.toModel(appointment));
    }

    /**
     * PUT-Endpunkt zum Aktualisieren eines vorhandenen Termins.
     * Es wird sichergestellt, dass die ID im Pfad und im RequestBody übereinstimmen.
     *
     * @param id          Die ID des zu aktualisierenden Termins (als Pfadvariable).
     * @param appointment Das aktualisierte Appointment-Objekt, das im RequestBody gesendet wird.
     * @return ResponseEntity mit dem aktualisierten HATEOAS-konformen EntityModel des Termins und HTTP-Status 200 OK.
     * @throws ResponseStatusException wenn die ID im Pfad und im RequestBody nicht übereinstimmen.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Appointment>> updateAppointment(@PathVariable Integer id, @RequestBody Appointment appointment) {
        // Sicherstellen, dass die ID im Pfad und im RequestBody übereinstimmen
        if (appointment.getId() == null || !appointment.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ungültige ID: Die ID im Pfad und im Body müssen übereinstimmen.");
        }
        Appointment updatedAppointment = calendarService.updateAppointment(appointment);
        return ResponseEntity.ok(assembler.toModel(updatedAppointment));
    }

    /**
     * DELETE-Endpunkt zum Löschen eines Termins anhand der angegebenen ID.
     * Nach erfolgreicher Löschung wird kein Inhalt zurückgegeben (HTTP-Status 204 No Content).
     *
     * @param id Die ID des zu löschenden Termins (als Pfadvariable).
     * @return ResponseEntity ohne Inhalt und mit HTTP-Status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Integer id) {
        calendarService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}
