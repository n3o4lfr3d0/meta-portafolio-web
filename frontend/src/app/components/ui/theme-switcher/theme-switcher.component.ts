import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-theme-switcher',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div id="theme-switcher-container" class="fixed top-6 right-6 z-50 flex flex-col gap-4 items-end animate-fade-in">
      <!-- Dynamic Title -->
      <div [class]="titleClasses()">
        {{ titleText() }}
      </div>

      <div [class]="containerClasses()">
        <!-- Red Pill (Light Mode / English) -->
        <button
          (click)="setTheme('light')"
          class="group relative w-8 h-8 rounded-full bg-red-600 shadow-[0_0_10px_rgba(220,38,38,0.6)] hover:scale-110 transition-transform border border-red-400"
          [class.ring-2]="themeService.theme() === 'light'"
          [class.ring-white]="themeService.theme() === 'light'"
        >
          <!-- Tooltip -->
          <div [class]="tooltipClasses()">
            English
          </div>
        </button>

        <!-- Blue Pill (Matrix Mode / Spanish) -->
        <button
          (click)="setTheme('matrix')"
          class="group relative w-8 h-8 rounded-full bg-blue-600 shadow-[0_0_10px_rgba(37,99,235,0.6)] hover:scale-110 transition-transform border border-blue-400"
          [class.ring-2]="themeService.theme() === 'matrix'"
          [class.ring-white]="themeService.theme() === 'matrix'"
        >
          <!-- Tooltip -->
          <div [class]="tooltipClasses()">
            Español
          </div>
        </button>
      </div>
    </div>
  `
})
export class ThemeSwitcherComponent {
  themeService = inject(ThemeService);

  titleText() {
    return this.themeService.language() === 'es' ? 'Elige tu idioma' : 'Choose your language';
  }

  titleClasses() {
    const base = "text-xs font-mono px-3 py-1 rounded mb-2 backdrop-blur-sm transition-all duration-300 border";
    if (this.themeService.theme() === 'matrix') {
      return `${base} bg-black/90 text-green-400 border-green-500/50 shadow-[0_0_15px_rgba(34,197,94,0.3)]`;
    } else {
      return `${base} bg-white/90 text-gray-800 border-gray-300 shadow-lg font-sans font-bold`;
    }
  }

  containerClasses() {
    const base = "flex gap-4 p-2 rounded-full backdrop-blur-md border transition-all duration-300";
    if (this.themeService.theme() === 'matrix') {
      return `${base} bg-black/40 border-white/10`;
    } else {
      return `${base} bg-white/40 border-black/10 shadow-md`;
    }
  }

  tooltipClasses() {
    const base = "absolute top-full mt-2 left-1/2 -translate-x-1/2 px-2 py-1 text-[10px] rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none z-50 border";
    if (this.themeService.theme() === 'matrix') {
      return `${base} bg-black/90 text-green-400 border-green-500/50 shadow-md font-mono`;
    } else {
      return `${base} bg-white text-gray-800 border-gray-200 shadow-lg font-sans font-medium`;
    }
  }

  setTheme(theme: 'matrix' | 'light') {
    this.themeService.toggleTheme(theme);
  }
}
