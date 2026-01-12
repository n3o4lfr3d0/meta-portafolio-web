import { ElementRef } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmationModalComponent } from './confirmation-modal.component';

describe('ConfirmationModalComponent', () => {
  let component: ConfirmationModalComponent;
  let fixture: ComponentFixture<ConfirmationModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmationModalComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ConfirmationModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have default inputs', () => {
    expect(component.title).toBe('');
    expect(component.type).toBe('delete');
  });

  it('should update inputs', () => {
    fixture.componentRef.setInput('title', 'New Title');
    fixture.componentRef.setInput('message', 'Message');
    fixture.componentRef.setInput('type', 'save');
    fixture.detectChanges();

    expect(component.title).toBe('New Title');
    expect(component.message).toBe('Message');
    expect(component.type).toBe('save');
  });

  it('should emit confirm event', () => {
    let emitted = false;
    component.confirm.subscribe(() => emitted = true);

    component.onConfirm();
    expect(emitted).toBe(true);
  });

  it('should emit cancel event and close', () => {
    // Mock the modal element
    const showModalSpy = vi.fn();
    const closeSpy = vi.fn();

    component.modal = {
      nativeElement: {
        showModal: showModalSpy,
        close: closeSpy
      }
    } as unknown as ElementRef<HTMLDialogElement>;

    let emitted = false;
    component.cancell.subscribe(() => emitted = true);

    component.onCancel();

    expect(emitted).toBe(true);
    expect(closeSpy).toHaveBeenCalled();
  });
});
