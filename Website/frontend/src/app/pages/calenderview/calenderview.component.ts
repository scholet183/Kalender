import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EventDialogComponent } from '../../helper/event-dialog/event-dialog.component';
import { UserDTO } from '../../models/userDTO.model';
import { Router } from '@angular/router';
import { MatButton } from '@angular/material/button';
import {AuthService} from "../../services/authService/auth.service";
import {CalendarService} from "../../services/calendarService/calendar.service";
import {Appointment} from "../../models/apointment.model";

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
    if (this.currentUser == null) {
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
            title: appointment.title,
            start: appointment.startDate,
            end: appointment.endDate
          }));
          this.calendarOptions = { ...this.calendarOptions, events: this.calendarEvents };
        },
        error: (error) => {
          console.error('Fehler beim Laden der Termine', error);
        }
      });
  }

  handleDateClick(arg: DateClickArg) {
    const dialogRef = this.dialog.open(EventDialogComponent, {
      data: { clickedDate: arg.dateStr }
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

        // Lokale Kalenderanzeige aktualisieren
        this.calendarEvents = [...this.calendarEvents, {
          title: newAppointment.title,
          start: newAppointment.startDate,
          end: newAppointment.endDate
        }];
        this.calendarOptions = { ...this.calendarOptions, events: this.calendarEvents };

        // Neuen Termin an das Backend senden
        this.calendarService.addAppointment(newAppointment)
          .subscribe({
            next: response => console.log('Termin erfolgreich gespeichert:', response),
            error: error => console.error('Fehler beim Speichern des Termins:', error)
          });
      }
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
