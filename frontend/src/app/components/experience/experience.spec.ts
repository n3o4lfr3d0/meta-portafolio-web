import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ExperienceService } from '../../services/experience.service';
import { ExperienceComponent } from './experience';

describe('ExperienceComponent', () => {
  let component: ExperienceComponent;
  let fixture: ComponentFixture<ExperienceComponent>;

  beforeEach(async () => {
    const experienceServiceMock = {
      getExperience: () => of([
        { title: 'Dev', company: 'Corp', period: '2020', description: 'Work', link: '' }
      ])
    };

    await TestBed.configureTestingModule({
      imports: [ExperienceComponent],
      providers: [
        { provide: ExperienceService, useValue: experienceServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ExperienceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
