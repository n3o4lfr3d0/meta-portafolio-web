import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { CommentsComponent } from './comments.component';
import { CommentService } from '../../services/comment.service';
import { ToastService } from '../../services/toast.service';

describe('CommentsComponent', () => {
  let component: CommentsComponent;
  let fixture: ComponentFixture<CommentsComponent>;
  let commentServiceMock: any;
  let toastServiceMock: any;

  beforeEach(async () => {
    commentServiceMock = {
      getApprovedComments: vi.fn().mockReturnValue(of([])),
      addComment: vi.fn().mockReturnValue(of({}))
    };

    toastServiceMock = {
      show: vi.fn()
    };

    await TestBed.configureTestingModule({
      imports: [CommentsComponent],
      providers: [
        { provide: CommentService, useValue: commentServiceMock },
        { provide: ToastService, useValue: toastServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CommentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load comments on init', () => {
    expect(commentServiceMock.getApprovedComments).toHaveBeenCalled();
  });

  it('should call addComment when form is valid and submitted', () => {
    component.commentForm.patchValue({
      username: 'Test User',
      content: 'Test Content'
    });
    
    component.onSubmit();
    
    expect(commentServiceMock.addComment).toHaveBeenCalled();
    expect(toastServiceMock.show).toHaveBeenCalledWith(expect.stringContaining('pendiente'), 'success');
  });
});
