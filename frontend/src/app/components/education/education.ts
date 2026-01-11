import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { EducationService } from '../../services/education.service';

@Component({
  selector: 'app-education',
  standalone: true,
  templateUrl: './education.html',
})
export class EducationComponent {
  private readonly educationService = inject(EducationService);
  education = toSignal(this.educationService.getEducation());
}
