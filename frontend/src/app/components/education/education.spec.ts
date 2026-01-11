import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { EducationService } from '../../services/education.service';
import { EducationComponent } from './education';

describe('EducationComponent', () => {
  let component: EducationComponent;
  let fixture: ComponentFixture<EducationComponent>;

  beforeEach(async () => {
    const educationServiceMock = {
      getEducation: () => of([
        { institution: 'Uni', degree: 'BSc', period: '2020', description: 'Desc', link: '' }
      ])
    };

    await TestBed.configureTestingModule({
      imports: [EducationComponent],
      providers: [
        { provide: EducationService, useValue: educationServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EducationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display education', () => {
    expect(component).toBeTruthy();
  });
});
