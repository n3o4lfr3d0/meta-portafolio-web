import { Component, computed, inject } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ProfileService } from '../../services/profile.service';
import { ThemeService } from '../../services/theme.service';
import { LogoComponent } from '../ui/logo/logo.component';

@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [LogoComponent],
  templateUrl: './hero.html'
})
export class HeroComponent {
  private readonly profileService = inject(ProfileService);
  public readonly themeService = inject(ThemeService);

  profile = toSignal(
    toObservable(this.themeService.language).pipe(
      switchMap(lang => this.profileService.getProfile(lang))
    )
  );
  cvUrl = computed(() => {
    const lang = this.themeService.language();
    const timestamp = new Date().getTime(); // Cache buster
    return lang === 'es' 
      ? `/cv/alfredo_soto_cv_es.pdf?v=${timestamp}` 
      : `/cv/alfredo_soto_cv_en.pdf?v=${timestamp}`;
  });
}
