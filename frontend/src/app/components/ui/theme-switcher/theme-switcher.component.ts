import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { ThemeService } from '../../../services/theme.service';

@Component({
  selector: 'app-theme-switcher',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="fixed bottom-6 right-6 z-50 flex flex-col gap-4 items-center animate-fade-in">
      <!-- Tooltip/Message -->
      <div class="bg-black/80 text-green-400 text-xs font-mono px-3 py-1 rounded border border-green-500/30 mb-2 backdrop-blur-sm">
        Choose your reality
      </div>

      <div class="flex gap-4 bg-black/40 p-2 rounded-full backdrop-blur-md border border-white/10">
        <!-- Red Pill (Light Mode) -->
        <button
          (click)="setTheme('light')"
          class="w-8 h-8 rounded-full bg-red-600 shadow-[0_0_10px_rgba(220,38,38,0.6)] hover:scale-110 transition-transform border border-red-400"
          [class.ring-2]="themeService.theme() === 'light'"
          [class.ring-white]="themeService.theme() === 'light'"
          title="Ignorance is bliss (Light Mode)"
        ></button>

        <!-- Blue Pill (Matrix Mode) -->
        <button
          (click)="setTheme('matrix')"
          class="w-8 h-8 rounded-full bg-blue-600 shadow-[0_0_10px_rgba(37,99,235,0.6)] hover:scale-110 transition-transform border border-blue-400"
          [class.ring-2]="themeService.theme() === 'matrix'"
          [class.ring-white]="themeService.theme() === 'matrix'"
          title="Stay in Wonderland (Matrix Mode)"
        ></button>
      </div>
    </div>
  `
})
export class ThemeSwitcherComponent {
  themeService = inject(ThemeService);

  setTheme(theme: 'matrix' | 'light') {
    this.themeService.toggleTheme(theme);
  }
}
