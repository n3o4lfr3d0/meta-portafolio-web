import { TestBed } from '@angular/core/testing';
import { ToastService } from './toast.service';

describe('ToastService', () => {
  let service: ToastService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ToastService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add a toast', () => {
    service.show('Test Message', 'success');
    expect(service.toasts().length).toBe(1);
    expect(service.toasts()[0].message).toBe('Test Message');
    expect(service.toasts()[0].type).toBe('success');
  });

  it('should remove a toast', () => {
    service.show('Test Message');
    const id = service.toasts()[0].id;
    service.remove(id);
    expect(service.toasts().length).toBe(0);
  });
});
