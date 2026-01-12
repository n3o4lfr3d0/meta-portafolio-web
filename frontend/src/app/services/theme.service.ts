import { effect, Injectable, signal } from '@angular/core';

export type Theme = 'matrix' | 'light';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {
  theme = signal<Theme>(this.getInitialTheme());
  language = signal<'es' | 'en'>('es');

  constructor() {
    effect(() => {
      const currentTheme = this.theme();
      localStorage.setItem('portfolio-theme', currentTheme);
      this.applyTheme(currentTheme);
      
      // Update language based on theme
      this.language.set(currentTheme === 'matrix' ? 'es' : 'en');
    });
  }

  toggleTheme(selectedTheme: Theme) {
    this.theme.set(selectedTheme);
  }

  private getInitialTheme(): Theme {
    const saved = localStorage.getItem('portfolio-theme') as Theme;
    return saved || 'matrix'; // Por defecto Matrix
  }

  private applyTheme(theme: Theme) {
    const body = document.body;
    const html = document.documentElement;

    if (theme === 'matrix') {
      // Matrix Mode is effectively Dark Mode
      body.classList.remove('theme-light');
      html.classList.add('dark');
    } else {
      // Light Mode
      body.classList.add('theme-light');
      html.classList.remove('dark');
    }
  }
}
