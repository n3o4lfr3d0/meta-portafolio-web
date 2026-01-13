import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ContactService } from '../../services/contact.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';
import { ContactComponent } from './contact.component';

describe('ContactComponent', () => {
  let component: ContactComponent;
  let fixture: ComponentFixture<ContactComponent>;
  let contactServiceMock: any;
  let themeServiceMock: any;
  let toastServiceMock: any;

  beforeEach(async () => {
    contactServiceMock = {
      submitContact: vi.fn().mockReturnValue(of('Success'))
    };

    themeServiceMock = {
      language: signal('en')
    };

    toastServiceMock = {
      show: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [ContactComponent, ReactiveFormsModule],
      providers: [
        { provide: ContactService, useValue: contactServiceMock },
        { provide: ThemeService, useValue: themeServiceMock },
        { provide: ToastService, useValue: toastServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have invalid form initially', () => {
    expect(component.contactForm.valid).toBeFalsy();
  });

  it('should call submitContact when form is valid', () => {
    component.contactForm.controls['name'].setValue('John Doe');
    component.contactForm.controls['email'].setValue('john@example.com');
    component.contactForm.controls['subject'].setValue('Hello');
    component.contactForm.controls['message'].setValue('This is a test message');

    component.onSubmit();

    expect(contactServiceMock.submitContact).toHaveBeenCalled();
    expect(toastServiceMock.show).toHaveBeenCalledWith(expect.stringContaining('successfully'), 'success');
  });

  it('should not call submitContact when form is invalid', () => {
    component.contactForm.controls['name'].setValue('');
    component.onSubmit();
    expect(contactServiceMock.submitContact).not.toHaveBeenCalled();
  });
});
