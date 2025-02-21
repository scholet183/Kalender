import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from "@angular/material/card";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {Router, RouterLink, RouterLinkActive} from "@angular/router";
import {UserService} from "../../services/user.service";
import {UserDTO} from "../../models/userDTO.model";

@Component({
  selector: 'app-registration',
  imports: [
    FormsModule,
    MatButton,
    MatCard,
    MatCardContent,
    MatCardHeader,
    MatCardTitle,
    MatFormField,
    MatInput,
    MatLabel,
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  name: string = '';
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router, private userService: UserService) {}

  onSubmit(): void {
    // Überprüfe, ob beide Felder ausgefüllt sind
    if (!this.name || !this.email || !this.password) {
      this.errorMessage = 'Bitte füllen Sie alle Felder aus.';
      return;
    }

    this.userService.addUser(this.name, this.email, this.password).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.log(err);
        this.errorMessage = 'Benutzererstellung fehlgeschlagen.';
      }
    });
  }

  routeToLogin(): void {
    this.router.navigate(['/login']);
  }

}
