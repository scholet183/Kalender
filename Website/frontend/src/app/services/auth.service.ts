import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserDTO } from '../models/userDTO.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject: BehaviorSubject<UserDTO | null> = new BehaviorSubject<UserDTO | null>(null);
  public user$: Observable<UserDTO | null> = this.userSubject.asObservable();

  // Methode zum Setzen des Benutzers (nach erfolgreichem Login)
  setUser(user: UserDTO): void {
    this.userSubject.next(user);
    localStorage.setItem('user', JSON.stringify(user));
  }

  // Methode zum Abrufen des aktuellen Benutzers
  getUser(): UserDTO | null {
    return this.userSubject.value;
  }

  // Methode zum Ausloggen
  logout(): void {
    this.userSubject.next(null);
    localStorage.removeItem('user');
  }
}
