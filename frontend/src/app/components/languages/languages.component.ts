import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { LanguageService } from '../../services/language.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-languages',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './languages.component.html'
})
export class LanguagesComponent {
  private readonly languageService = inject(LanguageService);
  public readonly themeService = inject(ThemeService); // Made public for template access

  languages = toSignal(
    toObservable(this.themeService.language).pipe(
      switchMap(lang => this.languageService.getAllLanguages(lang))
    ),
    { initialValue: [] }
  );
}
