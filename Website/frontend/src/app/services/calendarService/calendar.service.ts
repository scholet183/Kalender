import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {Appointment} from "../../models/apointment.model";

@Injectable({
  providedIn: 'root'
})
export class CalendarService {
  private baseUrl = 'http://localhost:8081/api/calendar';
  get baseURL(): string {
    return this.baseUrl;
  }

  constructor(private http: HttpClient) {}

  getAppointments(userId: number): Observable<Appointment[]> {
    const url = `${this.baseUrl}/getAppointments`;
    return this.http.get<Appointment[]>(url, { params: { userId: userId.toString() } });
  }

  addAppointment(appointment: Appointment): Observable<Appointment> {
    const url = `${this.baseUrl}/addAppointment`;
    return this.http.post<Appointment>(url, appointment);
  }
}
