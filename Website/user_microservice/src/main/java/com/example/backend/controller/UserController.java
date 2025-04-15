package com.example.backend.controller;

import com.example.backend.assembler.UserModelAssembler;
import com.example.backend.dto.UserDTO;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * REST-Controller zur Verwaltung von Benutzerdaten.
 * Dieser Controller stellt verschiedene Endpunkte zur Verfügung, um Benutzer zu speichern, abzurufen, zu aktualisieren und zu löschen.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Service-Schicht zur Verwaltung der Geschäftslogik rund um Benutzer.
     * @Autowired injiziert die benötigte Instanz zur Laufzeit.
     */
    @Autowired
    private UserService userService;

    /**
     * Assembler zur Umwandlung von UserDTO in ein HATEOAS-konformes EntityModel.
     * Dies erleichtert die Erweiterung der API mit Hypermedia-Links.
     */
    @Autowired
    private UserModelAssembler assembler;

    /**
     * POST-Endpunkt zum Speichern eines neuen Benutzers.
     * Nimmt ein UserDTO als Request Body entgegen, speichert den Benutzer und gibt das erzeugte EntityModel zurück.
     *
     * @param userDTO Das Benutzerobjekt, das gespeichert werden soll.
     * @return ResponseEntity mit Status 201 (Created) und einem Location-Header, der auf den neu erstellten Benutzer verweist.
     */
    @PostMapping("/save")
    public ResponseEntity<EntityModel<UserDTO>> saveUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.saveUser(userDTO);
        EntityModel<UserDTO> userModel = assembler.toModel(createdUser);
        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUser(createdUser.getId())).toUri())
                .body(userModel);
    }

    /**
     * GET-Endpunkt zum Abrufen aller Benutzer.
     * Es wird eine Liste aller Benutzer als HATEOAS CollectionModel zurückgegeben.
     *
     * @return ResponseEntity mit der Sammlung von Benutzern (jedes als EntityModel) und Status 200 (OK).
     */
    @GetMapping("/all")
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> getAllUsers() {
        List<EntityModel<UserDTO>> users = userService.getAllUsers().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<UserDTO>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    /**
     * GET-Endpunkt zum Abrufen eines einzelnen Benutzers anhand seiner ID.
     *
     * @param id Die eindeutige Benutzer-ID (über die Pfadvariable).
     * @return ResponseEntity mit dem HATEOAS-konformen EntityModel des angefragten Benutzers und Status 200 (OK).
     */
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUser(@PathVariable int id) {
        UserDTO userDTO = userService.getUserByID(id);
        return ResponseEntity.ok(assembler.toModel(userDTO));
    }

    /**
     * PUT-Endpunkt zum Aktualisieren eines bestehenden Benutzers.
     *
     * @param id      Die ID des zu aktualisierenden Benutzers (über Pfadvariable).
     * @param userDTO Das aktualisierte Benutzerobjekt (über Request Body).
     * @return ResponseEntity mit dem aktualisierten HATEOAS-konformen EntityModel und Status 200 (OK).
     */
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable int id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResponseEntity.ok(assembler.toModel(updatedUser));
    }

    /**
     * DELETE-Endpunkt zum Löschen eines Benutzers anhand seiner ID.
     *
     * @param id Die ID des zu löschenden Benutzers (über Pfadvariable).
     * @return ResponseEntity ohne Inhalt (Status 204 No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH-Endpunkt zum Aktualisieren des Benutzernamens eines bestimmten Benutzers.
     * Es werden nur teilweise Änderungen vorgenommen, d.h. nur der Name wird aktualisiert.
     *
     * @param id      Die ID des Benutzers, dessen Name aktualisiert werden soll.
     * @param userDTO Das Benutzerobjekt, das die neuen Namensinformationen enthält.
     * @return ResponseEntity ohne Inhalt (Status 204 No Content).
     */
    @PatchMapping("/update-name/{id}")
    public ResponseEntity<Void> updateUserName(@PathVariable int id, @RequestBody UserDTO userDTO) {
        userService.updateUserName(id, userDTO);
        return ResponseEntity.noContent().build();
    }

    /**
     * POST-Endpunkt zum Verarbeiten eines Benutzer-Logins.
     * Nimmt die Login-Daten als Request Body entgegen und gibt bei erfolgreichem Login das Benutzerobjekt zurück.
     *
     * @param userDTO Das Objekt, welches die Login-Daten enthält.
     * @return ResponseEntity mit dem HATEOAS-konformen EntityModel des eingeloggten Benutzers und Status 200 (OK).
     */
    @PostMapping("/login")
    public ResponseEntity<EntityModel<UserDTO>> login(@RequestBody UserDTO userDTO) {
        UserDTO loggedInUser = userService.login(userDTO);
        return ResponseEntity.ok(assembler.toModel(loggedInUser));
    }
}
