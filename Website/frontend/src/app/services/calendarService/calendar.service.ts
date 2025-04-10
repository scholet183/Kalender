import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Appointment } from '../../models/apointment.model';
import {response} from "express";

interface CalendarResponseWrapper {
  _embedded: {
    appointmentList: Appointment[];
  };
  _links: any;
}

interface AppointmentResponseWrapper {
  _links: any;
  id: number;
  userId: number;
  title: string;
  startDate: string;
  endDate: string;
  description: string;
  location: string;
}

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  public baseUrl = 'http://localhost:8081/api/calendar';

  constructor(private http: HttpClient) {}

  getAppointments(userId: number): Observable<Appointment[]> {
    const url = `${this.baseUrl}/getAppointments`;
    return this.http.get<CalendarResponseWrapper>(url, { params: { userId: userId.toString() } })
      .pipe(
        map(response => response._embedded.appointmentList)
      );
  }

  addAppointment(appointment: Appointment): Observable<Appointment> {
    const url = `${this.baseUrl}/addAppointment`;
    return this.http.post<AppointmentResponseWrapper>(url, appointment)
      .pipe(
        map(response => {
          const { _links, ...app } = response;
          return app as Appointment;
        })
      );
  }

  updateAppointment(appointment: Appointment): Observable<Appointment> {
    const url = `${this.baseUrl}/${appointment.id}`;
    return this.http.put<AppointmentResponseWrapper>(url, appointment)
      .pipe(
        map(response => {
          const {_links, ...app} = response;
          return app as Appointment;
        })
      )
  }

  deleteAppointment(id:number): Observable<Appointment> {
    const url = `${this.baseUrl}/${id}`;
    return this.http.delete<Appointment>(url);
  }
}
