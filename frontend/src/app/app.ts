import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ExperienceComponent } from './components/experience/experience';
import { HeroComponent } from './components/hero/hero';
import { ProjectsComponent } from './components/projects/projects';

import { SkillsComponent } from './components/skills/skills.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeroComponent, ExperienceComponent, ProjectsComponent, SkillsComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'frontend';
}
