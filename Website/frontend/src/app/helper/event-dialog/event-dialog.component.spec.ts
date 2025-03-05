import { ComponentFixture, TestBed } from '@angular/core/testing';
import { EventDialogComponent } from './event-dialog.component';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder } from '@angular/forms';

describe('EventDialogComponent', () => {
  let component: EventDialogComponent;
  let fixture: ComponentFixture<EventDialogComponent>;
  let dialogRefSpy: jasmine.SpyObj<MatDialogRef<EventDialogComponent>>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('MatDialogRef', ['close']);
    await TestBed.configureTestingModule({
      imports: [EventDialogComponent],
      providers: [
        FormBuilder,
        { provide: MatDialogRef, useValue: spy },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EventDialogComponent);
    component = fixture.componentInstance;
    dialogRefSpy = TestBed.inject(MatDialogRef) as jasmine.SpyObj<MatDialogRef<EventDialogComponent>>;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should close dialog with combined data, if form is valid', () => {
    // Arrange: Formular mit gültigen Werten füllen
    const title = 'Test Event';
    const startDate = new Date(2025, 2, 5); // 5. März 2025
    const endDate = new Date(2025, 2, 5);   // gleicher Tag
    component.form.setValue({
      title,
      startDate,
      startTime: '08:00',
      endDate,
      endTime: '09:00',
      description: 'Test Description',
      location: 'Test Location'
    });

    // Act: onSubmit aufrufen
    component.onSubmit();

    // Assert: Überprüfen, dass dialogRef.close mit den richtigen kombinierten Daten aufgerufen wurde
    expect(dialogRefSpy.close).toHaveBeenCalled();

    // Erwarte, dass die kombinierten Start- und End-Daten die korrekten Stunden und Minuten besitzen
    const callArg = dialogRefSpy.close.calls.mostRecent().args[0];
    expect(callArg.title).toBe(title);
    expect(callArg.description).toBe('Test Description');
    expect(callArg.location).toBe('Test Location');
    expect(callArg.start instanceof Date).toBeTrue();
    expect(callArg.end instanceof Date).toBeTrue();
    expect(callArg.start.getHours()).toBe(8);
    expect(callArg.start.getMinutes()).toBe(0);
    expect(callArg.end.getHours()).toBe(9);
    expect(callArg.end.getMinutes()).toBe(0);
  });

  it('should not close dialog, if form is invalid', () => {
    // Arrange: Einen ungültigen Zustand simulieren (leerer Titel)
    component.form.setValue({
      title: '',
      startDate: new Date(),
      startTime: '08:00',
      endDate: new Date(),
      endTime: '09:00',
      description: 'Test Description',
      location: 'Test Location'
    });

    // Act: onSubmit aufrufen
    component.onSubmit();

    // Assert: Es sollte kein Aufruf von dialogRef.close erfolgen
    expect(dialogRefSpy.close).not.toHaveBeenCalled();
  });

  it('should close dialog by canceling', () => {
    // Act: onCancel aufrufen
    component.onCancel();

    // Assert: dialogRef.close ohne Parameter aufrufen
    expect(dialogRefSpy.close).toHaveBeenCalled();
  });
});
