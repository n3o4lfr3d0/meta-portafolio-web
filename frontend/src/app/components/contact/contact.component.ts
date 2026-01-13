import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { environment } from '../../../environments/environment';
import { ContactService } from '../../services/contact.service';
import { ThemeService } from '../../services/theme.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './contact.component.html'
})
export class ContactComponent {
  private readonly fb = inject(FormBuilder);
  private readonly contactService = inject(ContactService);
  private readonly toastService = inject(ToastService);
  public readonly themeService = inject(ThemeService);

  contactForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2)]],
    email: ['', [Validators.required, Validators.email]],
    subject: ['', Validators.required],
    message: ['', [Validators.required, Validators.minLength(10)]]
  });

  isSubmitting = signal(false);

  // Labels based on language
  labels = {
    en: {
      title: 'Contact Me',
      subtitle: 'Get in touch for opportunities or just to say hi!',
      name: 'Name',
      namePlaceholder: 'Ex: John Doe',
      email: 'Email',
      emailPlaceholder: 'Ex: john@example.com',
      subject: 'Subject',
      subjectPlaceholder: 'Ex: Project Proposal',
      message: 'Message',
      messagePlaceholder: 'Ex: Hi, I would like to discuss...',
      send: 'Send Message',
      whatsapp: 'Chat on WhatsApp',
      success: 'Message sent successfully!',
      error: 'Failed to send message. Please try again.'
    },
    es: {
      title: 'Contáctame',
      subtitle: '¡Ponte en contacto para oportunidades o solo para saludar!',
      name: 'Nombre',
      namePlaceholder: 'Ej: Juan Pérez',
      email: 'Correo Electrónico',
      emailPlaceholder: 'Ej: juan@ejemplo.com',
      subject: 'Asunto',
      subjectPlaceholder: 'Ej: Propuesta de Proyecto',
      message: 'Mensaje',
      messagePlaceholder: 'Ej: Hola, me gustaría conversar sobre...',
      send: 'Enviar Mensaje',
      whatsapp: 'Hablemos por WhatsApp',
      success: '¡Mensaje enviado exitosamente!',
      error: 'Error al enviar el mensaje. Inténtalo de nuevo.'
    }
  };

  get t() {
    return this.labels[this.themeService.language()] || this.labels.en;
  }

  onSubmit() {
    if (this.contactForm.invalid) {
      this.contactForm.markAllAsTouched();
      return;
    }

    this.isSubmitting.set(true);
    this.contactService.submitContact(this.contactForm.value).subscribe({
      next: () => {
        this.toastService.show(this.t.success, 'success');
        this.contactForm.reset();
        this.isSubmitting.set(false);
      },
      error: () => {
        this.toastService.show(this.t.error, 'error');
        this.isSubmitting.set(false);
      }
    });
  }

  openWhatsApp() {
    const phoneNumber = environment.whatsappNumber;
    const message = this.themeService.language() === 'es'
      ? 'Hola, me gustaría contactarte.'
      : 'Hello, I would like to contact you.';
    window.open(`https://wa.me/${phoneNumber}?text=${encodeURIComponent(message)}`, '_blank');
  }
}
