import { DatePipe } from '@angular/common';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { of } from 'rxjs';
import { afterEach, beforeAll, beforeEach, describe, expect, it, vi } from 'vitest';
import { Comment } from '../../../models/comment.model';
import { AuthService } from '../../../services/auth.service';
import { CommentService } from '../../../services/comment.service';
import { ThemeService } from '../../../services/theme.service';
import { DashboardComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let commentServiceMock: any;
  let authServiceMock: any;
  let themeServiceMock: any;

  const mockComments: Comment[] = [
    { id: '1', username: 'User1', content: 'Content1', timestamp: "1/11/26", approved: false },
    { id: '2', username: 'User2', content: 'Content2', timestamp: "1/11/26", approved: true }
  ];

  beforeAll(() => {
    // Polyfill dialog methods if missing in JSDOM
    if (!HTMLDialogElement.prototype.showModal) {
      HTMLDialogElement.prototype.showModal = vi.fn();
    }
    if (!HTMLDialogElement.prototype.close) {
      HTMLDialogElement.prototype.close = vi.fn();
    }
  });

  beforeEach(async () => {
    commentServiceMock = {
      getAllComments: vi.fn().mockReturnValue(of(mockComments)),
      approveComment: vi.fn().mockReturnValue(of({})),
      deleteComment: vi.fn().mockReturnValue(of({}))
    };

    authServiceMock = {
      logout: vi.fn()
    };

    themeServiceMock = {
      toggleTheme: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [DashboardComponent, DatePipe],
      providers: [
        provideRouter([]),
        { provide: CommentService, useValue: commentServiceMock },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load comments on init', () => {
    expect(commentServiceMock.getAllComments).toHaveBeenCalled();
    expect(component.comments().length).toBe(2);
  });

  it('should approve comment', () => {
    component.approve('1');
    expect(commentServiceMock.approveComment).toHaveBeenCalledWith('1');
    expect(commentServiceMock.getAllComments).toHaveBeenCalledTimes(2); // Init + Approve
  });

  it('should open delete modal', () => {
    // Spy on the actual dialog element method
    // Since we are using document.getElementById in component, we must mock it or ensure it finds the element.
    // The component uses document.getElementById('delete_modal').
    // In JSDOM, checking fixture.nativeElement is safer, but if component code uses document.getElementById, we must rely on it finding the element in the document body.
    // Angular usually appends fixture to document body in Karma, but in Vitest/JSDOM we might need to be careful.
    // However, the cleanest way to test logic that uses document.getElementById is to spy on it.

    const mockDialog = {
      showModal: vi.fn(),
      close: vi.fn()
    } as any;

    const getElementByIdSpy = vi.spyOn(document, 'getElementById').mockReturnValue(mockDialog);

    component.openDeleteModal('1');
    expect(component.itemToDelete()).toBe('1');
    expect(getElementByIdSpy).toHaveBeenCalledWith('delete_modal');
    expect(mockDialog.showModal).toHaveBeenCalled();
  });

  it('should delete comment when confirmed', () => {
    component.itemToDelete.set('1');

    const mockDialog = {
      showModal: vi.fn(),
      close: vi.fn()
    } as any;

    vi.spyOn(document, 'getElementById').mockReturnValue(mockDialog);

    component.confirmDelete();
    expect(commentServiceMock.deleteComment).toHaveBeenCalledWith('1');
    expect(commentServiceMock.getAllComments).toHaveBeenCalledTimes(2);
    expect(mockDialog.close).toHaveBeenCalled();
  });

  it('should logout', () => {
    component.logout();
    expect(authServiceMock.logout).toHaveBeenCalled();
  });
});
