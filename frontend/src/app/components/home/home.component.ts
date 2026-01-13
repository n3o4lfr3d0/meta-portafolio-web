import { isPlatformBrowser } from '@angular/common';
import { AfterViewInit, Component, inject, OnInit, PLATFORM_ID } from '@angular/core';
import { SeoService } from '../../services/seo.service';
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
export class HomeComponent implements OnInit, AfterViewInit {
  private readonly tourService = inject(TourService);
  private readonly seoService = inject(SeoService);
  private readonly platformId = inject(PLATFORM_ID);

  ngOnInit() {
    this.seoService.setSeoData(
      'Fullstack Java & Angular Developer',
      'Portafolio profesional de Alfredo Soto. Desarrollador Fullstack especializado en Java Spring Boot, Angular y Cloud (AWS). Descubre mis proyectos y experiencia.'
    );
  }

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.waitForElementsAndStartTour();
    }
  }

  private waitForElementsAndStartTour(attempts = 0) {
    // Check for key elements across the page to ensure most sections are hydrated
    const heroSection = document.getElementById('hero-section');
    const experienceSection = document.getElementById('experiencia');
    const educationSection = document.getElementById('educacion');
    const languagesSection = document.querySelector('app-languages');
    const contactSection = document.getElementById('contacto');

    const areCriticalElementsReady = heroSection && experienceSection && educationSection;
    const isFullPageLikelyReady = areCriticalElementsReady && languagesSection && contactSection;

    // If we have critical elements and (all elements OR we've waited enough), start tour
    if (isFullPageLikelyReady || (areCriticalElementsReady && attempts > 10)) {
      // Add a small delay to ensure layout stability
      setTimeout(() => {
        // TEMPORARY: Disabled tour due to production issues
        // this.tourService.startTour();
      }, 500);
    } else if (attempts < 30) { // Wait up to 15 seconds (30 * 500ms)
      setTimeout(() => this.waitForElementsAndStartTour(attempts + 1), 500);
    } else {
      console.warn('Tour started with partial elements due to timeout');
      // TEMPORARY: Disabled tour due to production issues
      // this.tourService.startTour();
    }
  }
}
