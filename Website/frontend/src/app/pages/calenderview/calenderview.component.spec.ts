import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CalenderviewComponent } from './calenderview.component';
import {CalendarService} from "../../services/calendarService/calendar.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {AuthService} from "../../services/authService/auth.service";
import {MatDialog} from "@angular/material/dialog";
import {HttpClient, provideHttpClient} from "@angular/common/http";

describe('CalenderviewComponent', () => {
  let component: CalenderviewComponent;
  let fixture: ComponentFixture<CalenderviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        CalendarService,
        provideHttpClient(),
        AuthService,
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CalenderviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
