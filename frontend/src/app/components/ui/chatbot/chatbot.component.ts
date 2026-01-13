import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ChatService } from '../../../services/chat.service';
import { ThemeService } from '../../../services/theme.service';

interface ChatMessage {
  text: string;
  displayContent: SafeHtml;
  sender: 'user' | 'bot';
  options?: string[];
  timestamp: Date;
}

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrl: './chatbot.component.css'
})
export class ChatbotComponent {
  themeService = inject(ThemeService);
  chatService = inject(ChatService);
  sanitizer = inject(DomSanitizer);
  router = inject(Router);

  isOpen = signal(false);
  messages = signal<ChatMessage[]>([]);
  userInput = '';
  isLoading = signal(false);

  @ViewChild('scrollContainer') private readonly scrollContainer!: ElementRef;

  constructor() {
    effect(() => {
      // Auto-open chat slightly after load for engagement (optional)
      // setTimeout(() => this.isOpen.set(true), 2000);
    });

    effect(() => {
      // Scroll to bottom when messages change
      if (this.messages()) {
        setTimeout(() => this.scrollToBottom(), 100);
      }
    });
  }

  toggleChat() {
    this.isOpen.update(v => !v);
    // Always add welcome message if empty, OR if we want to refresh the welcome message based on language
    // But usually chat history is preserved.
    // However, if it's the first time opening, we add it.
    if (this.isOpen() && this.messages().length === 0) {
      this.addWelcomeMessage();
    }
  }

  addWelcomeMessage() {
    const isEs = this.themeService.language() === 'es';
    const text = isEs
      ? '¡Hola! Soy el asistente virtual de Alfredo. ¿En qué puedo ayudarte hoy?'
      : 'Hello! I am Alfredo\'s virtual assistant. How can I help you today?';

    // Check if the last message is a welcome message to update it if language changed?
    // No, that might be confusing. Just adding it on init is standard.
    // If user wants to clear chat, that's different.

    this.addBotMessage(text, isEs ? ['Experiencia', 'Habilidades', 'Educación', 'Contacto'] : ['Experience', 'Skills', 'Education', 'Contact']);
  }

  sendMessage() {
    if (!this.userInput.trim() || this.isLoading()) return;

    const text = this.userInput;
    this.addUserMessage(text);
    this.userInput = '';
    this.processInput(text);
  }

  addUserMessage(text: string) {
    this.messages.update(msgs => [...msgs, {
      text,
      displayContent: this.formatUserMessage(text),
      sender: 'user',
      timestamp: new Date()
    }]);
  }

  addBotMessage(text: string, options?: string[]) {
    this.messages.update(msgs => [...msgs, {
      text,
      displayContent: this.formatBotMessage(text),
      sender: 'bot',
      options,
      timestamp: new Date()
    }]);
  }

  private processInput(text: string) {
    const lowerText = text.toLowerCase();
    const isEs = this.themeService.language() === 'es';

    // Simple local logic for immediate actions
    if (lowerText.includes('linkedin')) {
      window.open('https://linkedin.com/in/alfredosotonolazco', '_blank');
      this.addBotMessage(isEs ? '¡Abriendo perfil de LinkedIn!' : 'Opening LinkedIn profile!');
      return;
    }

    this.isLoading.set(true);

    // Call Backend API
    const contextPage = this.router.url.replace('/', '') || 'home';
    this.chatService.sendMessage(text, this.themeService.language(), contextPage).subscribe({
      next: (res) => {
        this.addBotMessage(res.response);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error(err);
        let errorMessage = isEs
          ? 'Lo siento, tuve un problema conectando con mi cerebro digital. Por favor intenta de nuevo.'
          : 'Sorry, I had trouble connecting to my digital brain. Please try again.';

        if (err.message?.includes('503') || err.message?.includes('429') || err.status === 503 || err.status === 429) {
           errorMessage = isEs
             ? 'Mi cerebro está recibiendo demasiadas consultas ahora mismo. Por favor, espera unos segundos y pregúntame de nuevo.'
             : 'My brain is receiving too many requests right now. Please wait a few seconds and ask me again.';
        }

        this.addBotMessage(errorMessage);
        this.isLoading.set(false);
      }
    });
  }

  private formatUserMessage(text: string): SafeHtml {
     const escaped = text
      .replaceAll('&', "&amp;")
      .replaceAll('<', "&lt;")
      .replaceAll('>', "&gt;")
      .replaceAll('\n', '<br>');
     return this.sanitizer.bypassSecurityTrustHtml(escaped);
  }

  private formatBotMessage(text: string): SafeHtml {
    if (!text) return '';

    // 1. Try to convert Markdown tables first (if any)
    let processedText = this.convertMarkdownTableToHtml(text);

    // 2. Check if the response looks like HTML
    const hasHtmlTags = /<[a-z][\s\S]*>/i.test(processedText);

    if (hasHtmlTags) {
        return this.sanitizer.bypassSecurityTrustHtml(processedText);
    }

    // Fallback for non-HTML responses
    let html = processedText
      .replaceAll('&', "&amp;")
      .replaceAll('<', "&lt;")
      .replaceAll('>', "&gt;");

    // Bold formatting
    html = html.replaceAll(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // List formatting
    html = html.replaceAll(/(?:^|\n)(?:[*-])\s+(.*)/g, '<br>• $1');

    // Newline to <br>
    html = html.replaceAll('\n', '<br>');

    // Clean up multiple <br>
    html = html.replaceAll(/(<br>){3,}/g, '<br><br>');

    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  private convertMarkdownTableToHtml(text: string): string {
    const lines = text.split('\n');
    const ctx = { inTable: false, tableHtml: '', result: '' };

    for (const line of lines) {
        this.processLine(line.trim(), ctx);
    }

    if (ctx.inTable) {
        this.closeTable(ctx);
    }

    return ctx.result;
  }

  private processLine(line: string, ctx: { inTable: boolean, tableHtml: string, result: string }) {
      if (this.isTableLine(line)) {
          this.handleTableLine(line, ctx);
      } else {
          this.handleNonTableLine(line, ctx);
      }
  }

  private isTableLine(line: string): boolean {
      return line.startsWith('|') && (line.endsWith('|') || line.split('|').length > 2);
  }

  private handleTableLine(line: string, ctx: { inTable: boolean, tableHtml: string, result: string }) {
      if (!ctx.inTable) {
          ctx.inTable = true;
          ctx.tableHtml = '<table class="table table-bordered"><thead>';
      }

      if (line.includes('---')) {
          if (!ctx.tableHtml.includes('tbody')) {
              ctx.tableHtml += '</thead><tbody>';
          }
      } else {
          this.addRow(line, ctx);
      }
  }

  private addRow(line: string, ctx: { inTable: boolean, tableHtml: string, result: string }) {
      const cells = line.split('|').filter(c => c.trim() !== '');
      const tag = ctx.tableHtml.includes('tbody') ? 'td' : 'th';

      ctx.tableHtml += '<tr>';
      cells.forEach(cell => {
          ctx.tableHtml += `<${tag}>${cell.trim()}</${tag}>`;
      });
      ctx.tableHtml += '</tr>';
  }

  private handleNonTableLine(line: string, ctx: { inTable: boolean, tableHtml: string, result: string }) {
      if (ctx.inTable) {
          this.closeTable(ctx);
      }
      ctx.result += (ctx.result ? '\n' : '') + line;
  }

  private closeTable(ctx: { inTable: boolean, tableHtml: string, result: string }) {
      ctx.inTable = false;
      ctx.tableHtml += '</tbody></table>';
      ctx.result += ctx.tableHtml;
      ctx.tableHtml = '';
  }

  scrollToBottom() {
    if (this.scrollContainer?.nativeElement) {
        try {
            this.scrollContainer.nativeElement.scrollTop = this.scrollContainer.nativeElement.scrollHeight;
        } catch(err) {
            console.error('Error scrolling to bottom:', err);
        }
    }
  }

  handleOption(option: string) {
    this.userInput = option;
    this.sendMessage();
  }
}
