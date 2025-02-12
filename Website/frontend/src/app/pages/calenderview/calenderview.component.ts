import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin, { DateClickArg } from '@fullcalendar/interaction';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { EventDialogComponent } from '../../helper/event-dialog/event-dialog.component';


@Component({
  selector: 'app-calenderview',
  standalone: true,
  imports: [CommonModule, FullCalendarModule, HttpClientModule, MatDialogModule],
  templateUrl: './calenderview.component.html',
  styleUrls: ['./calenderview.component.css']
})
export class CalenderviewComponent {
  // Array für aktuell angezeigte Termine
  calendarEvents: any[] = [];

  // Konfiguration für FullCalendar
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, interactionPlugin],
    dateClick: this.handleDateClick.bind(this),
    events: this.calendarEvents
  };

  constructor(private http: HttpClient, private dialog: MatDialog) {}

  handleDateClick(arg: DateClickArg) {
    // Öffnet den Dialog und übergibt das angeklickte Datum als Default
    const dialogRef = this.dialog.open(EventDialogComponent, {
      data: { clickedDate: arg.dateStr }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // result enthält: { title, start, end }
        const newEvent = {
          title: result.title,
          start: result.start,
          end: result.end
        };

        // Neuen Termin lokal hinzufügen
        this.calendarEvents = [...this.calendarEvents, newEvent];
        // Options neu setzen, damit der Kalender aktualisiert wird
        this.calendarOptions = {
          ...this.calendarOptions,
          events: this.calendarEvents
        };

        // Erstmal nur als Mock: Sendet den neuen Termin an dein Backend
        // Wird später in eigener Service Klasse implementiert
        this.http.post('https://dein-backend-api/appointments', newEvent)
          .subscribe({
            next: response => console.log('Termin erfolgreich gespeichert:', response),
            error: error => console.error('Fehler beim Speichern des Termins:', error)
          });
      }
    });
  }
}
