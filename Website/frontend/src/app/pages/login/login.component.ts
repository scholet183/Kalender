import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { UserDTO } from '../../models/userDTO.model';
import { MatCard, MatCardContent, MatCardHeader, MatCardTitle } from '@angular/material/card';
import {MatError, MatFormField, MatLabel} from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [
    MatCardContent,
    MatCardTitle,
    MatCardHeader,
    MatCard,
    MatLabel,
    MatFormField,
    FormsModule,
    MatInput,
    MatButton,
    MatError
  ],
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  name: string = '';
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {}

  onSubmit(): void {
    // Überprüfe, ob alle Felder ausgefüllt sind
    if (!this.name || !this.email || !this.password) {
      this.errorMessage = 'Bitte füllen Sie alle Felder aus.';
      return;
    }

    // Angenommen, dein Login-Aufruf benötigt alle drei Parameter. Falls nicht, passe es an.
    this.userService.login(this.name, this.email, this.password).subscribe({
      next: (user: UserDTO) => {
        // Benutzer im AuthService speichern
        this.authService.setUser(user);
        // Navigiere zur Kalenderansicht
        this.router.navigate(['/calendar']);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Login fehlgeschlagen. Bitte überprüfen Sie Ihre Zugangsdaten.';
      }
    });
  }

  routeToRegistration(): void {
    this.router.navigate(['/register']);
  }
}
