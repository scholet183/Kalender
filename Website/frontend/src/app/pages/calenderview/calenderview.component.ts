import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { FullCalendarModule } from '@fullcalendar/angular';
import {CalendarOptions, EventClickArg} from '@fullcalendar/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatButton } from '@angular/material/button';
import { AuthService } from "../../services/authService/auth.service";
import { CalendarService } from "../../services/calendarService/calendar.service";
import { Appointment } from "../../models/apointment.model";
import { EventDialogComponent } from '../../helper/event-dialog/event-dialog.component';
import { UserDTO } from '../../models/userDTO.model';

@Component({
  selector: 'app-calenderview',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, MatDialogModule, MatButton],
  templateUrl: './calenderview.component.html',
  styleUrls: ['./calenderview.component.css']
})
export class CalenderviewComponent implements OnInit {
  calendarEvents: any[] = [];
  currentUser: UserDTO | null = null;

  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, interactionPlugin],
    dateClick: this.handleDateClick.bind(this),
    eventClick: this.handleEventClick.bind(this),  // Event-Click callback hinzufügen
    events: this.calendarEvents
  };

  constructor(
    private authService: AuthService,
    private calendarService: CalendarService,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();
    if (!this.currentUser) {
      this.router.navigate(['/login']);
      return;
    }
    this.fetchAppointments();
  }

  fetchAppointments(): void {
    this.calendarService.getAppointments(this.currentUser!.id!)
      .subscribe({
        next: (appointments) => {
          this.calendarEvents = appointments.map(appointment => ({
            id: appointment.id,
            title: appointment.title,
            start: appointment.startDate,
            end: appointment.endDate,
            extendedProps: appointment  // Speichert das gesamte Appointment-Objekt
          }));
          this.calendarOptions = { ...this.calendarOptions, events: this.calendarEvents };
        },
        error: (error) => {
          console.error('Fehler beim Laden der Termine', error);
        }
      });
  }

  // DateClick-Callback für das Erstellen
  handleDateClick(arg: DateClickArg) {
    const dialogRef = this.dialog.open(EventDialogComponent, {
      data: { action: 'create', clickedDate: arg.dateStr }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const newAppointment: Appointment = {
          title: result.title,
          startDate: result.start,
          endDate: result.end,
          userId: this.currentUser!.id!,
          description: result.description || '',
          location: result.location || ''
        };

        this.calendarService.addAppointment(newAppointment)
          .subscribe({
            next: response => {
              console.log('Termin erfolgreich gespeichert:', response);
              this.fetchAppointments();  // Kalender neu laden
            },
            error: error => console.error('Fehler beim Speichern des Termins:', error)
          });
      }
    });
  }

  handleEventClick(clickInfo: EventClickArg): void {
    // Annahme: extendedProps enthält das komplette Appointment-Objekt
    const appointment: Appointment = clickInfo.event.extendedProps as Appointment;

    // Öffnet den Dialog im Bearbeitungsmodus
    const editDialogRef = this.dialog.open(EventDialogComponent, {
      width: '400px',
      data: { action: 'edit', appointment: appointment }
    });

    editDialogRef.afterClosed().subscribe(result => {
      if (!result) {
        return;
      }
      // Falls der Lösch-Button gedrückt wurde
      if (result.delete) {
        this.calendarService.deleteAppointment(result.id)
          .subscribe({
            next: () => {
              clickInfo.event.remove();
              console.log('Termin erfolgreich gelöscht');
            },
            error: error => console.error('Fehler beim Löschen des Termins', error)
          });
      } else {
        // Andernfalls handelt es sich um ein Update des Termins
        const updatedAppointment: Appointment = {
          id: result.id,
          title: result.title,
          startDate: result.start,
          endDate: result.end,
          userId: appointment.userId, // Behalte die bestehende User-ID bei
          description: result.description || '',
          location: result.location || ''
        };
        this.calendarService.updateAppointment(updatedAppointment)
          .subscribe({
            next: updated => {
              console.log('Termin erfolgreich aktualisiert:', updated);
              this.fetchAppointments(); // Aktualisiert den Kalender
            },
            error: error => console.error('Fehler beim Aktualisieren des Termins', error)
          });
      }
    });
  }



  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
