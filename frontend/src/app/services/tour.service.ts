import { Injectable, inject } from '@angular/core';
import { driver } from 'driver.js';
import 'driver.js/dist/driver.css';
import { ThemeService } from './theme.service';

@Injectable({
  providedIn: 'root'
})
export class TourService {
  private readonly themeService = inject(ThemeService);

  startTour() {
    // TEMPORARY: Disabled check for testing in production
    // if (this.hasSeenTour()) {
    //   return;
    // }

    const isEs = this.themeService.language() === 'es';
    const steps = this.getSteps(isEs);
    const config = this.getDriverConfig(isEs, steps);

    const driverObj = driver(config);
    driverObj.drive();
  }

  private hasSeenTour(): boolean {
    return !!localStorage.getItem('mainTourSeen');
  }

  private getDriverConfig(isEs: boolean, steps: any[]) {
    return {
      showProgress: true,
      animate: true,
      doneBtnText: isEs ? 'Listo' : 'Done',
      nextBtnText: isEs ? 'Siguiente' : 'Next',
      prevBtnText: isEs ? 'Anterior' : 'Previous',
      steps: steps,
      onDestroyed: () => {
        localStorage.setItem('mainTourSeen', 'true');
      }
    };
  }

  private getSteps(isEs: boolean) {
    return TOUR_STEPS_CONFIG
      .filter(step => {
        const el = document.querySelector(step.element);
        return !!el;
      })
      .map(step => ({
        element: step.element,
        popover: isEs ? step.es : step.en
      }));
  }
}

const TOUR_STEPS_CONFIG = [
  {
    element: 'app-hero',
    es: { title: 'Bienvenido a mi Portafolio', description: 'Aquí puedes encontrar información sobre mi experiencia y habilidades.' },
    en: { title: 'Welcome to my Portfolio', description: 'Here you can find information about my experience and skills.' }
  },
  {
    element: '#repo-promo-link',
    es: { title: 'Código Fuente', description: 'Si te gusta este proyecto, ¡apóyalo con una estrella en GitHub!' },
    en: { title: 'Source Code', description: 'If you like this project, support it with a star on GitHub!' }
  },
  {
    element: '#experiencia',
    es: { title: 'Experiencia Laboral', description: 'Mi trayectoria profesional y los roles que he desempeñado.' },
    en: { title: 'Work Experience', description: 'My professional journey and the roles I have held.' }
  },
  {
    element: '#educacion',
    es: { title: 'Educación', description: 'Mi formación académica y certificaciones.' },
    en: { title: 'Education', description: 'My academic background and certifications.' }
  },
  {
    element: 'app-languages',
    es: { title: 'Idiomas', description: 'Los idiomas que domino y mi nivel de competencia.' },
    en: { title: 'Languages', description: 'The languages I speak and my proficiency level.' }
  },
  {
    element: '#tech-skills-title',
    es: { title: 'Habilidades Técnicas', description: 'Un resumen de mi stack tecnológico (Backend, Frontend, DevOps).' },
    en: { title: 'Technical Skills', description: 'An overview of my tech stack (Backend, Frontend, DevOps).' }
  },
  {
    element: '#soft-skills-section',
    es: { title: 'Habilidades Blandas', description: 'Competencias interpersonales clave para el trabajo en equipo.' },
    en: { title: 'Soft Skills', description: 'Key interpersonal skills for teamwork.' }
  },
  {
    element: '#comment-form',
    es: { title: 'Deja tu Huella', description: '¿Tienes feedback o sugerencias? Déjame un comentario para seguir mejorando.' },
    en: { title: 'Leave your Mark', description: 'Do you have feedback or suggestions? Leave a comment to keep improving.' }
  },
  {
    element: '#api-cards-section',
    es: { title: 'Entretenimiento y Recursos', description: 'Una sección dinámica que consume APIs para mostrar tips, frases y chistes diarios.' },
    en: { title: 'Entertainment & Resources', description: 'A dynamic section that consumes APIs to show daily tips, quotes, and jokes.' }
  },
  {
    element: '#contacto',
    es: { title: 'Contáctame', description: '¿Tienes alguna propuesta? Envíame un mensaje o contáctame por WhatsApp.' },
    en: { title: 'Contact Me', description: 'Have a proposal? Send me a message or contact me via WhatsApp.' }
  },
  {
    element: '#chatbot-container',
    es: { title: 'Asistente IA', description: 'Puedes chatear con mi asistente virtual potenciado por Gemini para saber más sobre mí.' },
    en: { title: 'AI Assistant', description: 'You can chat with my Gemini-powered virtual assistant to learn more about me.' }
  },
  {
    element: '#theme-switcher-container',
    es: { title: 'Personaliza tu experiencia', description: 'Cambia entre diferentes temas visuales según tu preferencia.' },
    en: { title: 'Customize your experience', description: 'Switch between different visual themes according to your preference.' }
  }
];
