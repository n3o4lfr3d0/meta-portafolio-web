import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { Comment } from '../../../models/comment.model';
import { AuthService } from '../../../services/auth.service';
import { CommentService } from '../../../services/comment.service';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  commentService = inject(CommentService);
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  comments = signal<Comment[]>([]);

  ngOnInit() {
    this.themeService.toggleTheme('light');
    this.loadComments();
  }

  loadComments() {
    this.commentService.getAllComments().subscribe(comments => {
      this.comments.set(comments);
    });
  }

  approve(id: string) {
    this.commentService.approveComment(id).subscribe(() => this.loadComments());
  }

  delete(id: string) {
    if (confirm('Are you sure you want to delete this comment?')) {
      this.commentService.deleteComment(id).subscribe(() => this.loadComments());
    }
  }

  logout() {
    this.authService.logout();
  }
}
