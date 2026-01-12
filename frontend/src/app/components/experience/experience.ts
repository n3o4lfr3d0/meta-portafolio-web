import { Component, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { ExperienceService } from '../../services/experience.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-experience',
  standalone: true,
  templateUrl: './experience.html',
})
export class ExperienceComponent {
  private readonly experienceService = inject(ExperienceService);
  public readonly themeService = inject(ThemeService);

  experience = toSignal(
    toObservable(this.themeService.language).pipe(
      switchMap(lang => this.experienceService.getExperience(lang))
    ),
    { initialValue: [] }
  );
}
