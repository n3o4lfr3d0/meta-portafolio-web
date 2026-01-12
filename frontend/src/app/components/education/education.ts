import { Component, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { EducationService } from '../../services/education.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-education',
  standalone: true,
  templateUrl: './education.html',
})
export class EducationComponent {
  private readonly educationService = inject(EducationService);
  public readonly themeService = inject(ThemeService);

  education = toSignal(
    toObservable(this.themeService.language).pipe(
      switchMap(lang => this.educationService.getEducation(lang))
    ),
    { initialValue: [] }
  );
}
