import { TestBed } from '@angular/core/testing';

import { CalendarService } from './calendar.service';
import {provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {UserService} from "../userService/user.service";

describe('CalendarService', () => {
  let service: CalendarService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        CalendarService
      ]
    });
    service = TestBed.inject(CalendarService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
