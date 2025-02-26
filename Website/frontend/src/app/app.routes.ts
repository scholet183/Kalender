import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login.component';
import { CalenderviewComponent } from './pages/calenderview/calenderview.component';
import {RegistrationComponent} from "./pages/registration/registration.component";

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'calendar', component:  CalenderviewComponent},
    { path: 'register', component:  RegistrationComponent},
    // Standardroute, wenn keine Route angegeben wird:
    { path: '', redirectTo: '/login', pathMatch: 'full' },
    // Wildcard-Route für ungültige URLs:
    { path: '**', redirectTo: '/login' }
  ];
