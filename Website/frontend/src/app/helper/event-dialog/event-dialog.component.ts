import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

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

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EventDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    // Nutzt das angeklickte Datum als Standard, oder heute
    const defaultDate = data && data.clickedDate ? new Date(data.clickedDate) : new Date();
    this.form = this.fb.group({
      title: ['', Validators.required],
      startDate: [defaultDate, Validators.required],
      startTime: ['08:00', Validators.required],
      endDate: [defaultDate, Validators.required],
      endTime: ['09:00', Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      const { title, startDate, startTime, endDate, endTime } = this.form.value;
      
      // Kombiniert Datum und Zeit für den Start
      const [startHour, startMinute] = startTime.split(':').map(Number);
      const start = new Date(startDate);
      start.setHours(startHour, startMinute);

      // Kombiniert Datum und Zeit für das Ende
      const [endHour, endMinute] = endTime.split(':').map(Number);
      const end = new Date(endDate);
      end.setHours(endHour, endMinute);

      // Schließt den Dialog und gibt die kombinierten Daten zurück
      this.dialogRef.close({ title, start, end });
    }
  }

  onCancel() {
    this.dialogRef.close();
  }
}
