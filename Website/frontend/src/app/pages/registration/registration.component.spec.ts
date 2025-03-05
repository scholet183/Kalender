import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { RegistrationComponent } from './registration.component';
import { UserService } from '../../services/userService/user.service';

describe('RegistrationComponent', () => {
  let component: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  let routerSpy: jasmine.SpyObj<Router>;
  let userServiceSpy: jasmine.SpyObj<UserService>;

  beforeEach(async () => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    userServiceSpy = jasmine.createSpyObj('UserService', ['addUser']);

    await TestBed.configureTestingModule({
      imports: [RegistrationComponent],
      providers: [
        { provide: Router, useValue: routerSpy },
        { provide: UserService, useValue: userServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegistrationComponent);
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
    expect(userServiceSpy.addUser).not.toHaveBeenCalled();
  });

  it('should successfully register a user and navigate to login', () => {
    // Arrange: Set valid input values
    component.name = 'Test User';
    component.email = 'test@example.com';
    component.password = 'password123';
    const returnedUserDTO = { id: 1, name: 'Test User', email: 'test@example.com', password: 'password123' };
    userServiceSpy.addUser.and.returnValue(of(returnedUserDTO));

    // Act
    component.onSubmit();

    // Assert
    expect(userServiceSpy.addUser).toHaveBeenCalledWith('Test User', 'test@example.com', 'password123');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should set errorMessage if registration fails', () => {
    // Arrange: Set valid input values but simulate an error from the addUser call
    component.name = 'Test User';
    component.email = 'test@example.com';
    component.password = 'password123';
    userServiceSpy.addUser.and.returnValue(throwError(() => new Error('Registration error')));

    // Act
    component.onSubmit();

    // Assert
    expect(userServiceSpy.addUser).toHaveBeenCalledWith('Test User', 'test@example.com', 'password123');
    expect(component.errorMessage).toBe('Benutzererstellung fehlgeschlagen.');
  });

  it('should navigate to login when routeToLogin is called', () => {
    // Act
    component.routeToLogin();

    // Assert
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
