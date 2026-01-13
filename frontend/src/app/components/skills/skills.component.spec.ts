import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Subject } from 'rxjs';
import { SkillService } from '../../services/skill.service';
import { SkillsComponent } from './skills.component';

describe('SkillsComponent', () => {
  let component: SkillsComponent;
  let fixture: ComponentFixture<SkillsComponent>;
  let skillServiceMock: any;
  let skillsSubject: Subject<any[]>;

  beforeEach(async () => {
    // Use a Subject to manually control when the data arrives
    skillsSubject = new Subject<any[]>();

    skillServiceMock = {
      getSkills: () => skillsSubject.asObservable()
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

    // Do NOT call detectChanges() yet if we want to test initial state perfectly,
    // but in Angular tests, we usually call it to initialize the component.
    // However, toSignal logic runs in injection context (constructor phase).
    // The observable pipe runs when subscribed.
    // toSignal subscribes immediately upon creation.

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should show loading skeleton initially (before data emits)', () => {
    // At this point, skillsSubject hasn't emitted anything.
    // So isLoading should be true (set in the first tap).
    expect(component.isLoading()).toBe(true);
    const skeleton = fixture.debugElement.query(By.css('.animate-pulse'));
    expect(skeleton).toBeTruthy();
  });

  it('should display skills after data emits', () => {
    // Emit data
    skillsSubject.next([
      { name: 'Angular', category: 'Frontend', level: 90, icon: 'angular' }
    ]);

    fixture.detectChanges();

    // Now isLoading should be false
    expect(component.isLoading()).toBe(false);

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Angular');
    expect(compiled.textContent).toContain('Frontend');

    // Skeleton should be gone
    const skeleton = fixture.debugElement.query(By.css('.animate-pulse'));
    expect(skeleton).toBeNull();
  });
});
