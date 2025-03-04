import { TestBed } from '@angular/core/testing';

import { AuthService } from './auth.service';
import {UserDTO} from "../../models/userDTO.model";

describe('AuthService', () => {
  let service: AuthService;

  const mockUser: UserDTO = {
    id: 1,
    name: 'Test Name',
    email: 'test@example.com',
    password: 'password123'
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AuthService]
    });
    service = TestBed.inject(AuthService);
    localStorage.clear();
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set the user correctly and update localStorage', () => {
    spyOn(localStorage, 'setItem').and.callThrough();

    // Act
    service.setUser(mockUser);

    // Assert
    expect(service.getUser()).toEqual(mockUser);
    expect(localStorage.setItem).toHaveBeenCalledWith('user', JSON.stringify(mockUser));
    service.user$.subscribe(user => {
      expect(user).toEqual(mockUser);
    });
  });

  it('should logout correctly by clearing the user and localStorage', () => {
    spyOn(localStorage, 'removeItem').and.callThrough();

    // Arrange
    service.setUser(mockUser);

    // Act
    service.logout();

    // Assert
    expect(service.getUser()).toBeNull();
    expect(localStorage.removeItem).toHaveBeenCalledWith('user');
    service.user$.subscribe(user => {
      expect(user).toBeNull();
    });
  });
});
