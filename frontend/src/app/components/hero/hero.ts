import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ProfileService } from '../../services/profile.service';
import { LogoComponent } from '../ui/logo/logo.component';

@Component({
  selector: 'app-hero',
  standalone: true,
  imports: [LogoComponent],
  templateUrl: './hero.html',
  styleUrl: './hero.css'
})
export class HeroComponent {
  private profileService = inject(ProfileService);
  profile = toSignal(this.profileService.getProfile());
}
