import { isPlatformBrowser } from '@angular/common';
import { AfterViewInit, Component, inject, PLATFORM_ID, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { driver } from 'driver.js';
import 'driver.js/dist/driver.css';
import { catchError, of, switchMap, tap } from 'rxjs';
import { DailyContentService } from '../../services/daily-content.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-api-cards',
  standalone: true,
  templateUrl: './api-cards.component.html',
})
export class ApiCardsComponent implements AfterViewInit {
  private readonly dailyContentService = inject(DailyContentService);
  public readonly themeService = inject(ThemeService);
  private readonly platformId = inject(PLATFORM_ID);

  isLoading = signal<boolean>(true);
  error = signal<string | null>(null);

  content = toSignal(
    toObservable(this.themeService.language).pipe(
      tap(() => {
        this.isLoading.set(true);
        this.error.set(null);
      }),
      switchMap(lang => 
        this.dailyContentService.getDailyContent(lang).pipe(
          tap(() => this.isLoading.set(false)),
          catchError(err => {
            console.error('Error loading daily content:', err);
            this.error.set(lang === 'es' ? 'No se pudo cargar el contenido diario.' : 'Could not load daily content.');
            this.isLoading.set(false);
            return of(null);
          })
        )
      )
    ),
    { initialValue: null }
  );

  ngAfterViewInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.checkAndStartTour();
    }
  }

  startTour() {
    const isEs = this.themeService.language() === 'es';
    const driverObj = driver({
      showProgress: true,
      steps: [
        { 
            element: '#api-cards-section', 
            popover: { 
                title: isEs ? 'Sección Dinámica' : 'Dynamic Section', 
                description: isEs ? 'Esta sección consume contenido generado diariamente.' : 'This section consumes daily generated content.' 
            } 
        },
        { 
            element: '#card-tip', 
            popover: { 
                title: isEs ? 'Tips Diarios' : 'Daily Tips', 
                description: isEs ? 'Consejos de programación curados para mejorar tu día.' : 'Curated programming tips to improve your day.' 
            } 
        },
        { 
            element: '#card-quote', 
            popover: { 
                title: isEs ? 'Inspiración' : 'Inspiration', 
                description: isEs ? 'Frases célebres fetched desde nuestra API en Spring Boot.' : 'Famous quotes fetched from our Spring Boot API.' 
            } 
        },
        { 
            element: '#card-joke', 
            popover: { 
                title: isEs ? 'Humor Geek' : 'Geek Humor', 
                description: isEs ? 'Porque reír también es parte del código.' : 'Because laughing is also part of the code.' 
            } 
        }
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
