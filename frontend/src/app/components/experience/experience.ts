import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ExperienceService } from '../../services/experience.service';

@Component({
  selector: 'app-experience',
  standalone: true,
  templateUrl: './experience.html',
})
export class ExperienceComponent {
  private readonly experienceService = inject(ExperienceService);
  experience = toSignal(this.experienceService.getExperience());
}
