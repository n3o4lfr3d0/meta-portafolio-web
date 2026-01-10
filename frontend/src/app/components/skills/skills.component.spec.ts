import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { SkillService } from '../../services/skill.service';
import { SkillsComponent } from './skills.component';

describe('SkillsComponent', () => {
  let component: SkillsComponent;
  let fixture: ComponentFixture<SkillsComponent>;

  beforeEach(async () => {
    // Mock simple sin depender de Jasmine
    const skillServiceMock = {
      getSkills: () => of([
        { name: 'Angular', category: 'Frontend', level: 90, icon: 'angular' }
      ])
    };

    await TestBed.configureTestingModule({
      imports: [SkillsComponent],
      providers: [
        { provide: SkillService, useValue: skillServiceMock }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SkillsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display skills', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Angular');
    expect(compiled.textContent).toContain('Frontend');
  });
});
