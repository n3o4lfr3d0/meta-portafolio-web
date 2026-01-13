import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';
import { switchMap, tap } from 'rxjs';
import { SkillService } from '../../services/skill.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './skills.component.html'
})
export class SkillsComponent {
  private readonly skillService = inject(SkillService);
  public readonly themeService = inject(ThemeService);

  isLoading = signal(true);

  skills = toSignal(
    toObservable(this.themeService.language).pipe(
      tap(() => this.isLoading.set(true)),
      switchMap(lang => this.skillService.getSkills(lang)),
      tap(() => this.isLoading.set(false))
    ),
    { initialValue: [] }
  );

  technicalSkills = computed(() => {
    const skills = this.skills().filter(s => s.category !== 'Soft Skills');
    const order = ['Backend', 'Cloud', 'DevOps', 'Frontend', 'QA', 'Tools'];

    return skills.sort((a, b) => {
      const indexA = order.indexOf(a.category);
      const indexB = order.indexOf(b.category);
      // If category not found in order list, put it at the end
      return (indexA === -1 ? 999 : indexA) - (indexB === -1 ? 999 : indexB);
    });
  });
  softSkills = computed(() => this.skills().filter(s => s.category === 'Soft Skills'));
}
