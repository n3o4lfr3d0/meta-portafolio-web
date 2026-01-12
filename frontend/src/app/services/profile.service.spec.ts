import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { Profile } from '../models/profile.model';
import { ProfileService } from './profile.service';

describe('ProfileService', () => {
  let service: ProfileService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProfileService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ProfileService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve profile data', () => {
    const dummyProfile: Profile = {
      name: 'Test User',
      title: 'Developer',
      summary: 'Summary',
      location: 'Location',
      experienceYears: '5',
      specialization: 'Java',
      socialLinks: []
    };

    service.getProfile().subscribe(profile => {
      expect(profile.name).toBe('Test User');
      expect(profile).toEqual(dummyProfile);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/profile?lang=es`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyProfile);
  });
});
