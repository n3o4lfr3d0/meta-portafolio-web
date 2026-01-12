import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { environment } from '../../environments/environment';
import { Comment } from '../models/comment.model';
import { CommentService } from './comment.service';

describe('CommentService', () => {
  let service: CommentService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        CommentService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(CommentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve approved comments', () => {
    const dummyComments: Comment[] = [
      { id: '1', username: 'User', content: 'Nice!', approved: true }
    ];

    service.getApprovedComments().subscribe(comments => {
      expect(comments.length).toBe(1);
      expect(comments).toEqual(dummyComments);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/comments`);
    expect(req.request.method).toBe('GET');
    req.flush(dummyComments);
  });

  it('should add a comment', () => {
    const newComment: Comment = { username: 'New User', content: 'Great job!', approved: false };
    const savedComment: Comment = { ...newComment, id: '123' };

    service.addComment(newComment).subscribe(comment => {
      expect(comment).toEqual(savedComment);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/comments`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newComment);
    req.flush(savedComment);
  });
});
