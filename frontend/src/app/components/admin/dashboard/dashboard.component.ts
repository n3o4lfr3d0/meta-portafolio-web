import { DatePipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Comment } from '../../../models/comment.model';
import { AuthService } from '../../../services/auth.service';
import { CommentService } from '../../../services/comment.service';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  commentService = inject(CommentService);
  authService = inject(AuthService);
  themeService = inject(ThemeService);
  comments = signal<Comment[]>([]);

  // UX State
  actionInProgress = signal<string | null>(null);
  itemToDelete = signal<string | null>(null);

  ngOnInit() {
    this.themeService.toggleTheme('matrix');
    this.loadComments();
  }

  loadComments() {
    this.commentService.getAllComments().subscribe(comments => {
      this.comments.set(comments);
    });
  }

  approve(id: string) {
    this.actionInProgress.set(id);
    this.commentService.approveComment(id).subscribe({
      next: () => {
        this.loadComments();
        this.actionInProgress.set(null);
      },
      error: () => {
        this.actionInProgress.set(null);
        // Could show toast here
      }
    });
  }

  openDeleteModal(id: string) {
    this.itemToDelete.set(id);
    const modal = document.getElementById('delete_modal') as HTMLDialogElement;
    if (modal) modal.showModal();
  }

  closeDeleteModal() {
    this.itemToDelete.set(null);
    const modal = document.getElementById('delete_modal') as HTMLDialogElement;
    if (modal) modal.close();
  }

  confirmDelete() {
    const id = this.itemToDelete();
    if (!id) return;

    this.actionInProgress.set(id);
    this.closeDeleteModal(); // Close modal immediately to show loading on row or keep it open?
    // Better UX: Close modal, show loading on row (since we set actionInProgress)

    this.commentService.deleteComment(id).subscribe({
      next: () => {
        this.loadComments();
        this.actionInProgress.set(null);
      },
      error: () => {
        this.actionInProgress.set(null);
      }
    });
  }

  logout() {
    this.authService.logout();
  }
}
