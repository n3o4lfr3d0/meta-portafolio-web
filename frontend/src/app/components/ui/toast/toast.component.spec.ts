import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ToastService } from '../../../services/toast.service';
import { ToastComponent } from './toast.component';

describe('ToastComponent', () => {
  let component: ToastComponent;
  let fixture: ComponentFixture<ToastComponent>;
  let toastService: ToastService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ToastComponent],
      providers: [ToastService]
    }).compileComponents();

    fixture = TestBed.createComponent(ToastComponent);
    component = fixture.componentInstance;
    toastService = TestBed.inject(ToastService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display toasts from service', () => {
    toastService.show('Hello World', 'info');
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Hello World');
    expect(compiled.querySelector('.alert-info')).toBeTruthy();
  });

  it('should display correct style for success', () => {
    toastService.show('Success!', 'success');
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.alert-success')).toBeTruthy();
  });
});
