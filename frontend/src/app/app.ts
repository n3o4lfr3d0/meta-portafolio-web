import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ApiCardsComponent } from './components/api-cards/api-cards.component';
import { EducationComponent } from './components/education/education';
import { ExperienceComponent } from './components/experience/experience';
import { HeroComponent } from './components/hero/hero';
import { SkillsComponent } from './components/skills/skills.component';
import { MatrixRainComponent } from './components/ui/matrix-rain/matrix-rain.component';
import { ThemeSwitcherComponent } from './components/ui/theme-switcher/theme-switcher.component';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeroComponent,
    ExperienceComponent,
    EducationComponent,
    SkillsComponent,
    ApiCardsComponent,
    MatrixRainComponent,
    ThemeSwitcherComponent
  ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'frontend';
  themeService = inject(ThemeService);
}
