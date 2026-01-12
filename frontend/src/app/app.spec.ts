import { TestBed } from '@angular/core/testing';
import { App } from './app';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ProfileService } from './services/profile.service';
import { ExperienceService } from './services/experience.service';
import { SkillService } from './services/skill.service';
import { LanguageService } from './services/language.service';
import { CommentService } from './services/comment.service';
import { of } from 'rxjs';

describe('App', () => {
  beforeEach(async () => {
    // Mocks simples que devuelven observables vacíos o datos dummy
    const profileServiceMock = {
      getProfile: () => of({
        name: 'Test',
        title: 'Dev',
        summary: 'Sum',
        location: 'Loc',
        experienceYears: '5',
        specialization: 'Java',
        socialLinks: []
      })
    };
    const experienceServiceMock = {
      getExperience: () => of([])
    };
    const skillServiceMock = {
      getSkills: () => of([])
    };
    const languageServiceMock = {
      getAllLanguages: () => of([])
    };
    const commentServiceMock = {
      getApprovedComments: () => of([]),
      addComment: () => of({})
    };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ProfileService, useValue: profileServiceMock },
        { provide: ExperienceService, useValue: experienceServiceMock },
        { provide: SkillService, useValue: skillServiceMock },
        { provide: LanguageService, useValue: languageServiceMock },
        { provide: CommentService, useValue: commentServiceMock }
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render toast component', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges(); 
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('app-toast')).toBeTruthy();
  });
});
