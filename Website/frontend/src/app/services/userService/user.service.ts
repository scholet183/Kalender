import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserDTO } from '../../models/userDTO.model';

interface UserResponseWrapper {
  _links: any;
  id: number;
  name: string;
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  public baseUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  login(name: string, email: string, password: string): Observable<UserDTO> {
    const loginData: UserDTO = { name, email, password };
    return this.http.post<UserResponseWrapper>(`${this.baseUrl}/login`, loginData)
      .pipe(
        map(response => {
          // Hier kannst du optional auch die Links nutzen oder loggen
          const { _links, ...user } = response;
          return user as UserDTO;
        })
      );
  }

  addUser(name: string, email: string, password: string): Observable<UserDTO> {
    const newUserData: UserDTO = { name, email, password };
    return this.http.post<UserResponseWrapper>(`${this.baseUrl}/save`, newUserData)
      .pipe(
        map(response => {
          const { _links, ...user } = response;
          return user as UserDTO;
        })
      );
  }
}
