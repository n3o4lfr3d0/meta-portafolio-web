import { TestBed } from '@angular/core/testing';
import { driver } from 'driver.js';
import { describe, expect, it, vi } from 'vitest';
import { ThemeService } from './theme.service';
import { TourService } from './tour.service';

// Mock driver.js
vi.mock('driver.js', () => ({
  driver: vi.fn().mockReturnValue({
    drive: vi.fn()
  })
}));

describe('TourService', () => {
  let service: TourService;
  let themeServiceMock: any;

  beforeEach(() => {
    // Mock ThemeService
    themeServiceMock = {
      language: vi.fn().mockReturnValue('es') // Default to Spanish for test
    };

    TestBed.configureTestingModule({
      providers: [
        TourService,
        { provide: ThemeService, useValue: themeServiceMock }
      ]
    });
    service = TestBed.inject(TourService);

    // Clear localStorage
    localStorage.clear();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should NOT start tour if localStorage has mainTourSeen', () => {
    localStorage.setItem('mainTourSeen', 'true');
    service.startTour();
    expect(driver).not.toHaveBeenCalled();
  });

  it('should start tour if localStorage does not have mainTourSeen', () => {
    service.startTour();
    expect(driver).toHaveBeenCalled();
  });

  it('should configure driver with correct steps', () => {
    service.startTour();

    const calls = (driver as any).mock.calls;
    const driverCall = calls[calls.length - 1][0];
    const steps = driverCall.steps;

    expect(steps.length).toBe(12); // Updated step count

    // Validate Chatbot Step Selector
    const chatbotStep = steps.find((s: any) => s.element === '#chatbot-container');
    expect(chatbotStep).toBeTruthy();
    expect(chatbotStep.popover.title).toBe('Asistente IA');

    // Validate Theme Switcher Step Selector
    const themeStep = steps.find((s: any) => s.element === '#theme-switcher-container');
    expect(themeStep).toBeTruthy();
    expect(themeStep.popover.title).toBe('Personaliza tu experiencia');

    // Validate API Cards Step
    const apiCardsStep = steps.find((s: any) => s.element === '#api-cards-section');
    expect(apiCardsStep).toBeTruthy();
    expect(apiCardsStep.popover.title).toBe('Entretenimiento y Recursos');

    // Validate Tech Skills Title Step
    const techSkillsStep = steps.find((s: any) => s.element === '#tech-skills-title');
    expect(techSkillsStep).toBeTruthy();
    expect(techSkillsStep.popover.title).toBe('Habilidades Técnicas');

    // Validate Soft Skills Section Step
    const softSkillsStep = steps.find((s: any) => s.element === '#soft-skills-section');
    expect(softSkillsStep).toBeTruthy();
    expect(softSkillsStep.popover.title).toBe('Habilidades Blandas');

    // Validate Comments Step
    const commentsStep = steps.find((s: any) => s.element === '#comment-form');
    expect(commentsStep).toBeTruthy();
    expect(commentsStep.popover.title).toBe('Deja tu Huella');
  });

  it('should start tour in Spanish by default', () => {
    // Mock ThemeService to return 'es' (which is default for 'matrix' theme)
    // Note: The service is already mocked in beforeEach, but we can verify the behavior
    // based on the default setup

    service.startTour();

    const calls = (driver as any).mock.calls;
    const driverCall = calls[calls.length - 1][0];

    expect(driverCall.doneBtnText).toBe('Listo'); // Spanish text
    expect(driverCall.nextBtnText).toBe('Siguiente'); // Spanish text

    const heroStep = driverCall.steps.find((s: any) => s.element === 'app-hero');
    expect(heroStep.popover.title).toBe('Bienvenido a mi Portafolio');
  });

  it('should use English text when language is not es', () => {
    themeServiceMock.language.mockReturnValue('en');
    service.startTour();

    const calls = (driver as any).mock.calls;
    const driverCall = calls[calls.length - 1][0];

    const steps = driverCall.steps;
    const chatbotStep = steps.find((s: any) => s.element === '#chatbot-container');
    expect(chatbotStep.popover.title).toBe('AI Assistant');
  });
});
