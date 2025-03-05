import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { UserService } from '../../services/userService/user.service';
import { AuthService } from '../../services/authService/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let routerSpy: jasmine.SpyObj<Router>;
  let userServiceSpy: jasmine.SpyObj<UserService>;
  let authServiceSpy: jasmine.SpyObj<AuthService>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    userServiceSpy = jasmine.createSpyObj('UserService', ['login']);
    authServiceSpy = jasmine.createSpyObj('AuthService', ['setUser']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: UserService, useValue: userServiceSpy },
        { provide: AuthService, useValue: authServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should set errorMessage if not all fields are filled', () => {
    // Arrange: Leave all fields empty
    component.name = '';
    component.email = '';
    component.password = '';

    // Act
    component.onSubmit();

    // Assert
    expect(component.errorMessage).toBe('Bitte füllen Sie alle Felder aus.');
    expect(userServiceSpy.login).not.toHaveBeenCalled();
  });

  it('should successfully log in and navigate to the calendar view', () => {
    // Arrange: Set valid input values
    component.name = 'Test User';
    component.email = 'test@example.com';
    component.password = 'password123';
    const userDTO = { id: 1, name: 'Test User', email: 'test@example.com' };
    const returnedUserDTO = { id: 1, name: 'Test User', email: 'test@example.com', password: 'password123' };
    userServiceSpy.login.and.returnValue(of(returnedUserDTO));

    // Act
    component.onSubmit();

    // Assert
    expect(userServiceSpy.login).toHaveBeenCalledWith('Test User', 'test@example.com', 'password123');
    // Use jasmine.objectContaining to compare objects and ignore potential extra properties.
    expect(authServiceSpy.setUser).toHaveBeenCalledWith(jasmine.objectContaining(userDTO));
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/calendar']);
  });

  it('should set errorMessage if login fails', () => {
    // Arrange: Set valid input values but simulate a login error
    component.name = 'Test User';
    component.email = 'test@example.com';
    component.password = 'password123';
    userServiceSpy.login.and.returnValue(throwError(() => new Error('Login error')));

    // Act
    component.onSubmit();

    // Assert
    expect(userServiceSpy.login).toHaveBeenCalledWith('Test User', 'test@example.com', 'password123');
    expect(component.errorMessage).toBe('Login fehlgeschlagen. Bitte überprüfen Sie Ihre Zugangsdaten.');
  });

  it('should navigate to registration page when routeToRegistration is called', () => {
    // Act
    component.routeToRegistration();

    // Assert
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/register']);
  });
});
