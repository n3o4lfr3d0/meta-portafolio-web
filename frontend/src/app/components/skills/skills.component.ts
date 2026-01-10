import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SkillService } from '../../services/skill.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './skills.component.html'
})
export class SkillsComponent {
  private skillService = inject(SkillService);
  skills = toSignal(this.skillService.getSkills(), { initialValue: [] });
}
