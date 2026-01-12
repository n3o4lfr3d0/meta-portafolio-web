import { Component } from '@angular/core';
import { ApiCardsComponent } from '../api-cards/api-cards.component';
import { CommentsComponent } from '../comments/comments.component';
import { EducationComponent } from '../education/education';
import { ExperienceComponent } from '../experience/experience';
import { HeroComponent } from '../hero/hero';
import { LanguagesComponent } from '../languages/languages.component';
import { SkillsComponent } from '../skills/skills.component';
import { FooterComponent } from '../ui/footer/footer.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    HeroComponent,
    ExperienceComponent,
    EducationComponent,
    SkillsComponent,
    LanguagesComponent,
    ApiCardsComponent,
    CommentsComponent,
    FooterComponent
  ],
  templateUrl: './home.component.html'
})
export class HomeComponent {}
