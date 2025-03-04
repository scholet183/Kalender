import { TestBed } from '@angular/core/testing';

import { CalendarService } from './calendar.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {Appointment} from "../../models/apointment.model";

describe('CalendarService', () => {
  let service: CalendarService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(CalendarService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should getAppointments correctly', () => {
    //Arrange
    let userID: number = 1;
    const inputAppointments: Appointment[] = [
      {
        userId: 1,
        title: "testAppointment1",
        startDate: "2025-02-20T09:00:00Z",
        endDate: "2025-03-20T09:00:00Z",
        description: "this meeting will be important",
        location: "Teams-Meeting",
      },
      {
        userId: 1,
        title: "testAppointment2",
        startDate: "2025-02-20T09:00:00Z",
        endDate: "2025-03-20T09:00:00Z",
        description: "this meeting will be not so important",
        location: "Room-E325",
      },
    ]
    //Act
    service.getAppointments(userID).subscribe(
      (resultAppointments: Appointment[]) => {
        expect(resultAppointments).toEqual(inputAppointments);
      }
    );
    //Assert
    const request = httpTestingController.expectOne(
      service.baseURL + "/getAppointments?userId=" + userID,
    );
    expect(request.request.method).toEqual("GET");
    request.flush(inputAppointments);
  });

  it('should addAppointment correctly', () => {
    //Arrange
    const appointment: Appointment =
      {
        userId: 1,
        title: "testAppointment1",
        startDate: "2025-02-20T09:00:00Z",
        endDate: "2025-03-20T09:00:00Z",
        description: "this meeting will be important",
        location: "Teams-Meeting",
      };
    //Act
    service.addAppointment(appointment).subscribe(
      (returnedAppointment: Appointment) => {
        expect(returnedAppointment).toEqual(appointment);
      }
    );
    //Assert
    const request = httpTestingController.expectOne(
      service.baseURL + "/addAppointment",
    );
    expect(request.request.method).toEqual("POST");
    request.flush(appointment);
  });
});
