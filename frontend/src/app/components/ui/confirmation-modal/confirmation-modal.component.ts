import { CommonModule } from '@angular/common';
import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-confirmation-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './confirmation-modal.component.html'
})
export class ConfirmationModalComponent {
  @Input() title = '';
  @Input() message = '';
  @Input() actionLabel = 'Confirm';
  @Input() cancelLabel = 'Cancel';
  @Input() type: 'delete' | 'save' = 'delete';
  @Input() processing = false;
  @Input() error = '';

  @Output() confirm = new EventEmitter<void>();
  @Output() cancell = new EventEmitter<void>();

  @ViewChild('modal') modal!: ElementRef<HTMLDialogElement>;

  show() {
    this.modal.nativeElement.showModal();
  }

  close() {
    this.modal.nativeElement.close();
  }

  onCancel() {
    this.close();
    this.cancell.emit();
  }

  onConfirm() {
    this.confirm.emit();
  }
}
