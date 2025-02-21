import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UserDTO} from "../../models/userDTO.model";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  login(name: string, email: string, password: string): Observable<UserDTO> {
    const loginData: UserDTO = { name, email, password };
    return this.http.post<UserDTO>(`${this.baseUrl}/login`, loginData);
  }

  addUser(name: string, email: string, password: string): Observable<UserDTO> {
    const newUserData: UserDTO = { name, email, password };
    return this.http.post<UserDTO>(`${this.baseUrl}/save`, newUserData);
  }
}
