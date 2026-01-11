import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ProfileService } from '../../services/profile.service';
import { LogoComponent } from '../ui/logo/logo.component';
import { HeroComponent } from './hero';

describe('HeroComponent', () => {
  let component: HeroComponent;
  let fixture: ComponentFixture<HeroComponent>;
  let profileServiceMock: any;

  const mockProfile = {
    name: 'Test Name',
    title: 'Test Title',
    summary: 'Test Summary',
    location: 'Test Location',
    experienceYears: '5+',
    specialization: 'Fullstack',
    socialLinks: []
  };

  beforeEach(async () => {
    profileServiceMock = {
      getProfile: vi.fn().mockReturnValue(of(mockProfile))
    };

    await TestBed.configureTestingModule({
      imports: [HeroComponent, LogoComponent],
      providers: [
        { provide: ProfileService, useValue: profileServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HeroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load profile data', () => {
    expect(profileServiceMock.getProfile).toHaveBeenCalled();
    // Since it uses toSignal, we might need to check the signal value or template
    // However, checking component.profile() is direct
    const profile = component.profile();
    expect(profile).toEqual(mockProfile);
  });
});
