import { CommonModule } from '@angular/common';
import { Component, inject, signal, ViewChild } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BehaviorSubject, switchMap } from 'rxjs';
import { Comment } from '../../models/comment.model';
import { CommentService } from '../../services/comment.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';
import { ConfirmationModalComponent } from '../ui/confirmation-modal/confirmation-modal.component';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ConfirmationModalComponent],
  templateUrl: './comments.component.html'
})
export class CommentsComponent {
  private readonly fb = inject(FormBuilder);
  private readonly commentService = inject(CommentService);
  public readonly themeService = inject(ThemeService);
  private readonly toastService = inject(ToastService);

  @ViewChild(ConfirmationModalComponent) confirmationModal!: ConfirmationModalComponent;

  private readonly refreshTrigger = new BehaviorSubject<void>(undefined);
  comments = toSignal(
    this.refreshTrigger.pipe(
      switchMap(() => this.commentService.getApprovedComments())
    ),
    { initialValue: [] }
  );

  commentForm = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(2)]],
    content: ['', [Validators.required, Validators.minLength(5)]]
  });

  isSubmitting = signal(false);

  // Editing state
  editingCommentId = signal<string | null>(null);
  editContentControl = this.fb.control('', [Validators.required, Validators.minLength(5)]);

  // Modal State
  modalTitle = signal('');
  modalMessage = signal('');
  modalActionLabel = signal('');
  modalType = signal<'delete' | 'save'>('delete');
  modalError = signal('');
  isModalProcessing = signal(false);
  targetCommentId = signal<string | null>(null);

  onSubmit() {
    if (this.commentForm.valid) {
      this.isSubmitting.set(true);
      const newComment: Comment = this.commentForm.value as Comment;

      this.commentService.addComment(newComment).subscribe({
        next: (createdComment) => {
          this.isSubmitting.set(false);

          // Save deletion token to localStorage
          if (createdComment.deletionToken) {
            this.saveDeletionToken(createdComment.id!, createdComment.deletionToken);
          }

          const msg = this.themeService.language() === 'es'
            ? '¡Gracias! Tu comentario ha sido enviado y está pendiente de aprobación.'
            : 'Thank you! Your comment has been submitted and is pending approval.';

          this.toastService.show(msg, 'success');
          this.commentForm.reset();

          // Refresh comments list to show the new one (if approved) or just to be safe
          this.refreshComments();
        },
        error: () => {
          this.isSubmitting.set(false);
          this.toastService.show(
            this.themeService.language() === 'es' ? 'Error al enviar comentario' : 'Error submitting comment',
            'error'
          );
        }
      });
    }
  }

  refreshComments() {
    this.refreshTrigger.next();
  }

  saveDeletionToken(commentId: string, token: string) {
    const tokens = JSON.parse(localStorage.getItem('my_comments') || '{}');
    tokens[commentId] = token;
    localStorage.setItem('my_comments', JSON.stringify(tokens));
  }

  getDeletionToken(commentId: string): string | null {
    const tokens = JSON.parse(localStorage.getItem('my_comments') || '{}');
    return tokens[commentId] || null;
  }

  // --- Edit Logic ---

  startEdit(comment: Comment) {
    this.editingCommentId.set(comment.id!);
    this.editContentControl.setValue(comment.content);
  }

  cancelEdit() {
    this.editingCommentId.set(null);
    this.editContentControl.reset();
  }

  confirmSave(commentId: string) {
    const token = this.getDeletionToken(commentId);
    if (!token || this.editContentControl.invalid) return;

    this.targetCommentId.set(commentId);
    this.modalTitle.set(this.themeService.language() === 'es' ? '¿Guardar cambios?' : 'Save changes?');
    this.modalMessage.set(this.themeService.language() === 'es'
      ? 'Tu comentario volverá a estado pendiente de aprobación.'
      : 'Your comment will return to pending approval status.');
    this.modalActionLabel.set(this.themeService.language() === 'es' ? 'Guardar' : 'Save');
    this.modalType.set('save');
    this.modalError.set('');
    this.isModalProcessing.set(false);

    this.openModal();
  }

  // --- Delete Logic ---

  confirmDelete(id: string) {
    this.targetCommentId.set(id);
    this.modalTitle.set(this.themeService.language() === 'es' ? '¿Eliminar comentario?' : 'Delete comment?');
    this.modalMessage.set(this.themeService.language() === 'es'
      ? 'Esta acción no se puede deshacer.'
      : 'This action cannot be undone.');
    this.modalActionLabel.set(this.themeService.language() === 'es' ? 'Eliminar' : 'Delete');
    this.modalType.set('delete');
    this.modalError.set('');
    this.isModalProcessing.set(false);

    this.openModal();
  }

  // --- Modal Logic ---

  openModal() {
    this.confirmationModal.show();
  }

  closeModal() {
    this.confirmationModal.close();
    this.targetCommentId.set(null);
    this.modalError.set('');
    this.isModalProcessing.set(false);
  }

  executeAction() {
    const id = this.targetCommentId();
    if (!id) return;

    const token = this.getDeletionToken(id);
    if (!token) {
      this.modalError.set(this.themeService.language() === 'es'
        ? 'No tienes permiso para esta acción (Token faltante).'
        : 'You do not have permission for this action (Missing token).');
      return;
    }

    this.isModalProcessing.set(true);
    this.modalError.set('');

    if (this.modalType() === 'save') {
      const newContent = this.editContentControl.value!;
      this.commentService.updateComment(id, newContent, token).subscribe({
        next: () => {
          this.isModalProcessing.set(false);
                this.editingCommentId.set(null);
                this.closeModal();
                this.refreshComments();

                // Show success message (Edit pending)
                const msg = this.themeService.language() === 'es'
                  ? 'Tu edición ha sido enviada y está pendiente de aprobación.'
                  : 'Your edit has been submitted for approval.';
                this.toastService.show(msg, 'success');
              },
              error: (err) => {
          console.error(err);
          this.isModalProcessing.set(false);
          this.modalError.set(this.themeService.language() === 'es'
            ? 'Error al actualizar. Intenta de nuevo.'
            : 'Error updating. Please try again.');
        }
      });
    } else if (this.modalType() === 'delete') {
      this.commentService.deleteComment(id, token).subscribe({
        next: () => {
          this.isModalProcessing.set(false);
          this.closeModal();
          this.refreshComments();
          this.toastService.show(
            this.themeService.language() === 'es' ? 'Comentario eliminado correctamente.' : 'Comment deleted successfully.',
            'success'
          );
        },
        error: (err) => {
          console.error(err);
          this.isModalProcessing.set(false);
          this.modalError.set(this.themeService.language() === 'es'
            ? 'Error al eliminar. Puede que ya no exista.'
            : 'Error deleting. It may not exist.');
        }
      });
    }
  }
}
