import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EventDialogComponent } from '../../helper/event-dialog/event-dialog.component';
import { AuthService } from '../../services/auth.service';
import { UserDTO } from '../../models/userDTO.model';
import { Router } from '@angular/router';
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-calenderview',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, HttpClientModule, MatDialogModule, MatButton],
  templateUrl: './calenderview.component.html',
  styleUrls: ['./calenderview.component.css']
})
export class CalenderviewComponent {
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
    private http: HttpClient,
    private dialog: MatDialog,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getUser();
  }

  handleDateClick(arg: DateClickArg) {
    const dialogRef = this.dialog.open(EventDialogComponent, {
      data: { clickedDate: arg.dateStr }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const newEvent = {
          title: result.title,
          start: result.start,
          end: result.end
        };

        // Termin lokal hinzufügen
        this.calendarEvents = [...this.calendarEvents, newEvent];
        // Kalender-Optionen aktualisieren, damit der Kalender den neuen Termin anzeigt
        this.calendarOptions = {
          ...this.calendarOptions,
          events: this.calendarEvents
        };

        // Neuen Termin an dein Backend senden (hier als Beispiel-URL)
        this.http.post('https://dein-backend-api/appointments', newEvent)
          .subscribe({
            next: response => console.log('Termin erfolgreich gespeichert:', response),
            error: error => console.error(error)
          });
      }
    });
  }

  logout(): void {
    // Benutzer ausloggen und auf Login-Seite navigieren
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
