import { isPlatformBrowser } from '@angular/common';
import { AfterViewInit, Component, inject, OnInit, PLATFORM_ID, signal } from '@angular/core';
import { driver } from 'driver.js';
import 'driver.js/dist/driver.css';
import { DailyContent, DailyContentService } from '../../services/daily-content.service';

@Component({
  selector: 'app-api-cards',
  standalone: true,
  templateUrl: './api-cards.component.html',
})
export class ApiCardsComponent implements OnInit, AfterViewInit {
  private readonly dailyContentService = inject(DailyContentService);
  private readonly platformId = inject(PLATFORM_ID);

  content = signal<DailyContent | null>(null);
  isLoading = signal<boolean>(true);
  error = signal<string | null>(null);

  ngOnInit() {
    this.loadContent();
  }

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.checkAndStartTour();
    }
  }

  loadContent() {
    this.isLoading.set(true);
    console.log('Initiating daily content fetch...');
    this.dailyContentService.getDailyContent().subscribe({
      next: (data) => {
        console.log('Daily content received:', data);
        this.content.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        console.error('Error loading daily content:', err);
        this.error.set('No se pudo cargar el contenido diario.');
        this.isLoading.set(false);
      }
    });
  }

  startTour() {
    const driverObj = driver({
      showProgress: true,
      steps: [
        { element: '#api-cards-section', popover: { title: 'Sección Dinámica', description: 'Esta sección consume contenido generado diariamente.' } },
        { element: '#card-tip', popover: { title: 'Tips Diarios', description: 'Consejos de programación curados para mejorar tu día.' } },
        { element: '#card-quote', popover: { title: 'Inspiración', description: 'Frases célebres fetched desde nuestra API en Spring Boot.' } },
        { element: '#card-joke', popover: { title: 'Humor Geek', description: 'Porque reír también es parte del código.' } }
      ]
    });
    driverObj.drive();
    localStorage.setItem('tourSeen', 'true');
  }

  private checkAndStartTour() {
    const tourSeen = localStorage.getItem('tourSeen');
    if (!tourSeen) {
      // Delay slightly to ensure rendering
      setTimeout(() => this.startTour(), 2000);
    }
  }
}
