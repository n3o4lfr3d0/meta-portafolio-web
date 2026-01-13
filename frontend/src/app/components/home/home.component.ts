import { isPlatformBrowser } from '@angular/common';
import { AfterViewInit, Component, inject, PLATFORM_ID } from '@angular/core';
import { TourService } from '../../services/tour.service';
import { ApiCardsComponent } from '../api-cards/api-cards.component';
import { CommentsComponent } from '../comments/comments.component';
import { ContactComponent } from '../contact/contact.component';
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
    ContactComponent,
    FooterComponent
  ],
  templateUrl: './home.component.html'
})
export class HomeComponent implements AfterViewInit {
  private readonly tourService = inject(TourService);
  private readonly platformId = inject(PLATFORM_ID);

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      // Small delay to ensure everything is rendered
      setTimeout(() => {
        this.tourService.startTour();
      }, 1500);
    }
  }
}
