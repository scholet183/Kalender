import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import {MatDialogRef, MAT_DIALOG_DATA, MatDialogModule, MatDialog} from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {ConfirmDeleteDialogComponent} from "../confirm-delete-dialog/confirm-delete-dialog.component";

@Component({
  selector: 'app-event-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './event-dialog.component.html',
  styleUrls: ['./event-dialog.component.css']
})
export class EventDialogComponent {
  form: FormGroup;
  dialogTitle: string = 'Neuen Termin erstellen';
  action: 'create' | 'edit' = 'create';

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EventDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private dialog: MatDialog  // MatDialog für den Bestätigungsdialog
  ) {
    if (data && data.action) {
      this.action = data.action;
    }
    // Nutzt das angeklickte Datum als Standard oder heute
    const defaultDate = data && data.clickedDate ? new Date(data.clickedDate) : new Date();
    this.form = this.fb.group({
      title: ['', Validators.required],
      startDate: [defaultDate, Validators.required],
      startTime: ['08:00', Validators.required],
      endDate: [defaultDate, Validators.required],
      endTime: ['09:00', Validators.required],
      description: [''],
      location: ['']
    });

    // Falls im Bearbeitungsmodus, vorhandene Termindaten vorbefüllen
    if (this.action === 'edit' && data.appointment) {
      this.dialogTitle = 'Termin bearbeiten';
      const appointment = data.appointment;
      const start = new Date(appointment.startDate);
      const end = new Date(appointment.endDate);
      const pad = (num: number) => num.toString().padStart(2, '0');
      const startTime = `${pad(start.getHours())}:${pad(start.getMinutes())}`;
      const endTime = `${pad(end.getHours())}:${pad(end.getMinutes())}`;

      this.form.setValue({
        title: appointment.title || '',
        startDate: start,
        startTime: startTime,
        endDate: end,
        endTime: endTime,
        description: appointment.description || '',
        location: appointment.location || ''
      });
    }
  }

  onSubmit() {
    if (this.form.valid) {
      const { title, startDate, startTime, endDate, endTime, description, location } = this.form.value;
      // Kombiniere Datum und Zeit für Start und Ende
      const [startHour, startMinute] = startTime.split(':').map(Number);
      const start = new Date(startDate);
      start.setHours(startHour, startMinute);
      const [endHour, endMinute] = endTime.split(':').map(Number);
      const end = new Date(endDate);
      end.setHours(endHour, endMinute);
      // Ergebnisobjekt vorbereiten
      const result: any = { title, start, end, description, location };
      if (this.action === 'edit' && this.data.appointment) {
        result.id = this.data.appointment.id;
      }
      this.dialogRef.close(result);
    }
  }

  onDelete() {
    // Öffne zuerst einen Bestätigungsdialog
    this.dialog.open(ConfirmDeleteDialogComponent, {
      width: '300px'
    }).afterClosed().subscribe(confirmed => {
      if (confirmed) {
        // Wenn "Ja" gewählt wurde, signalisiere die Löschaktion an den aufrufenden Component
        this.dialogRef.close({ delete: true, id: this.data.appointment?.id });
      }
    });
  }

  onCancel() {
    this.dialogRef.close();
  }
}
