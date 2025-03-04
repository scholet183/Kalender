import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventDialogComponent } from './event-dialog.component';
import {MAT_DIALOG_DATA, MatDialog, MatDialogRef} from "@angular/material/dialog";

describe('EventDialogComponent', () => {
  let component: EventDialogComponent;
  let fixture: ComponentFixture<EventDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EventDialogComponent],
      providers: [
        { provide: MatDialogRef, useValue: {} },
        { provide: MAT_DIALOG_DATA, useValue: {} },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(EventDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
