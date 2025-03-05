import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CalenderviewComponent } from './calenderview.component';
import { CalendarService } from '../../services/calendarService/calendar.service';
import { AuthService } from '../../services/authService/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { DateClickArg } from '@fullcalendar/interaction';
import {Appointment} from "../../models/apointment.model";

describe('CalenderviewComponent', () => {
  let component: CalenderviewComponent;
  let fixture: ComponentFixture<CalenderviewComponent>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;
  let calendarServiceSpy: jasmine.SpyObj<CalendarService>;
  let dialogSpy: jasmine.SpyObj<MatDialog>;
  let routerSpy: jasmine.SpyObj<Router>;

  const mockUser = { id: 1, name: 'Test User', email: 'test@example.com', password: 'example' };

  beforeEach(async () => {
    authServiceSpy = jasmine.createSpyObj('AuthService', ['getUser', 'logout']);
    calendarServiceSpy = jasmine.createSpyObj('CalendarService', ['getAppointments', 'addAppointment']);
    dialogSpy = jasmine.createSpyObj('MatDialog', ['open']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    // Set default spy behavior for getUser and getAppointments
    authServiceSpy.getUser.and.returnValue(mockUser);
    calendarServiceSpy.getAppointments.and.returnValue(of([]));

    await TestBed.configureTestingModule({
      imports: [CalenderviewComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: CalendarService, useValue: calendarServiceSpy },
        { provide: MatDialog, useValue: dialogSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CalenderviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect to login if no current user is found', () => {
    // Arrange: Override getUser to return null
    authServiceSpy.getUser.and.returnValue(null);
    fixture = TestBed.createComponent(CalenderviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // Assert: Expect navigation to login
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should fetch appointments if a current user is found', () => {
    // Arrange: Provide a mock appointment array
    const appointments = [
      {
        userId: 1,
        title: 'Appointment 1',
        startDate: '2025-03-05T08:00:00Z',
        endDate: '2025-03-05T09:00:00Z'
      }
    ];
    calendarServiceSpy.getAppointments.and.returnValue(of(appointments));

    // Reinitialize the component to trigger ngOnInit with the new spy behavior
    fixture = TestBed.createComponent(CalenderviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    // Assert: Verify getAppointments was called and calendarEvents are updated
    expect(calendarServiceSpy.getAppointments).toHaveBeenCalledWith(mockUser.id);
    expect(component.calendarEvents.length).toBe(1);
    expect(component.calendarEvents[0]).toEqual({
      title: 'Appointment 1',
      start: '2025-03-05T08:00:00Z',
      end: '2025-03-05T09:00:00Z'
    });
    expect(component.calendarOptions.events).toEqual(component.calendarEvents);
  });

  it('should log out and navigate to login', () => {
    // Act: Call logout
    component.logout();

    // Assert: Verify that logout is called on the authService and navigation to login is triggered
    expect(authServiceSpy.logout).toHaveBeenCalled();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
