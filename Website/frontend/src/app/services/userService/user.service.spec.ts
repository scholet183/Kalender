import { TestBed } from '@angular/core/testing';

import { UserService } from './user.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {UserDTO} from "../../models/userDTO.model";

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should login correctly', () => {
    //Arrange
    const userDTO: UserDTO = {
      id: 1,
      name: "Test User",
      email: "test@test.com",
      password: "test123",
    }
    //Act
    service.login(userDTO.name, userDTO.email, userDTO.password).subscribe(
      (responseUser: UserDTO) => {
        expect(responseUser).toEqual(userDTO);
      }
    );
    //Asert
    const request = httpTestingController.expectOne(
      service.baseUrl + `/login`
    );
    expect(request.request.method).toEqual('POST');
    request.flush(userDTO);
  });

  it('should addUser correctly', () => {
    //Arrange
    const userDTO: UserDTO = {
      id: 1,
      name: "Test User",
      email: "test@test.com",
      password: "test123",
    }
    //Act
    service.addUser(userDTO.name, userDTO.email, userDTO.password).subscribe(
      (responseUser: UserDTO) => {
        expect(responseUser).toEqual(userDTO);
      }
    );
    //Assert
    const request = httpTestingController.expectOne(
      service.baseUrl + `/save`
    );
    expect(request.request.method).toEqual('POST');
    request.flush(userDTO);
  });

});
