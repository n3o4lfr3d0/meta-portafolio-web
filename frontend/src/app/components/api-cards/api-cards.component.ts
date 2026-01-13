import { Component, inject, PLATFORM_ID, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import 'driver.js/dist/driver.css';
import { catchError, of, switchMap, tap } from 'rxjs';
import { DailyContentService } from '../../services/daily-content.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-api-cards',
  standalone: true,
  templateUrl: './api-cards.component.html',
})
export class ApiCardsComponent {
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
}
