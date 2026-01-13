import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { beforeEach, describe, expect, it, vi } from 'vitest';
import { ChatService } from '../../../services/chat.service';
import { ThemeService } from '../../../services/theme.service';
import { ChatbotComponent } from './chatbot.component';

describe('ChatbotComponent', () => {
  let component: ChatbotComponent;
  let fixture: ComponentFixture<ChatbotComponent>;
  let themeServiceMock: any;
  let chatServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    themeServiceMock = {
      theme: signal('matrix'),
      language: signal('es')
    };

    chatServiceMock = {
        sendMessage: vi.fn().mockReturnValue(of({ response: 'Bot response' }))
    };

    routerMock = {
        url: '/home'
    };

    await TestBed.configureTestingModule({
      imports: [ChatbotComponent],
      providers: [
        { provide: ThemeService, useValue: themeServiceMock },
        { provide: ChatService, useValue: chatServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ChatbotComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should be closed initially', () => {
    expect(component.isOpen()).toBe(false);
    const chatWindow = fixture.debugElement.query(By.css('.w-80'));
    expect(chatWindow).toBeFalsy();
  });

  it('should open when toggle button is clicked', () => {
    const toggleBtn = fixture.debugElement.query(By.css('button.w-14'));
    toggleBtn.nativeElement.click();
    fixture.detectChanges();

    expect(component.isOpen()).toBe(true);
    const chatWindow = fixture.debugElement.query(By.css('.w-80'));
    expect(chatWindow).toBeTruthy();
  });

  it('should add initial greeting when opened', () => {
    component.toggleChat();
    fixture.detectChanges();

    const messages = component.messages();
    expect(messages.length).toBeGreaterThan(0);
    expect(messages[0].sender).toBe('bot');
  });

  it('should send user message and receive bot response', () => {
    component.toggleChat();
    fixture.detectChanges();

    const form = fixture.debugElement.query(By.css('form'));

    // Simulate user typing (directly setting model to avoid NG0100 and ensure sync update)
    component.userInput = 'Hello';

    // Trigger submit without intermediate detectChanges to avoid ExpressionChangedAfterItHasBeenCheckedError
    // because userInput changes from '' -> 'Hello' -> '' (in sendMessage) within one check cycle if we aren't careful.
    form.triggerEventHandler('submit', null);
    fixture.detectChanges();

    const messages = component.messages();
    // User message + Bot response (async but mocked observable returns immediately)
    // Wait, addBotMessage is called in subscribe next.
    expect(messages.length).toBeGreaterThan(1);
    const lastMsg = messages.at(-1);
    expect(lastMsg?.text).toBe('Bot response');
    expect(lastMsg?.sender).toBe('bot');
    expect(chatServiceMock.sendMessage).toHaveBeenCalledWith('Hello', 'es', 'home');
  });

  it('should render markdown table correctly', () => {
      component.toggleChat();
      const tableMarkdown = "Here is a table:\n| Header 1 | Header 2 |\n| --- | --- |\n| Cell 1 | Cell 2 |";

      component.addBotMessage(tableMarkdown);
      fixture.detectChanges();

      // Need to find the message in DOM
      // The messages loop tracks by timestamp, so we need to ensure the view updates
      const msgElements = fixture.debugElement.queryAll(By.css('.whitespace-pre-wrap'));
      const lastMsgEl = msgElements.at(-1);

      // We look for table tag inside the innerHTML
      const table = lastMsgEl?.query(By.css('table'));
      expect(table).toBeTruthy();
      expect(table?.nativeElement.innerHTML).toContain('Header 1');
      expect(table?.nativeElement.innerHTML).toContain('Cell 1');
  });

  it('should handle API error gracefully', () => {
      component.toggleChat();
      chatServiceMock.sendMessage.mockReturnValue(throwError(() => ({ status: 429 })));

      component.userInput = 'Hello';
      component.sendMessage();
      fixture.detectChanges();

      const messages = component.messages();
      const lastMsg = messages.at(-1);
      expect(lastMsg?.text).toContain('demasiadas consultas');
  });
});
