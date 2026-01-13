import { DatePipe } from '@angular/common';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
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
        { provide: CommentService, useValue: commentServiceMock },
        { provide: AuthService, useValue: authServiceMock },
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
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

  it('should delete comment when confirmed', () => {
    vi.spyOn(globalThis, 'confirm').mockReturnValue(true);
    component.delete('1');
    expect(commentServiceMock.deleteComment).toHaveBeenCalledWith('1');
    expect(commentServiceMock.getAllComments).toHaveBeenCalledTimes(2);
  });

  it('should NOT delete comment when NOT confirmed', () => {
    vi.spyOn(globalThis, 'confirm').mockReturnValue(false);
    component.delete('1');
    expect(commentServiceMock.deleteComment).not.toHaveBeenCalled();
  });

  it('should logout', () => {
    component.logout();
    expect(authServiceMock.logout).toHaveBeenCalled();
  });
});
