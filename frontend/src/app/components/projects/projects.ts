import { Component, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-projects',
  standalone: true,
  templateUrl: './projects.html',
})
export class ProjectsComponent {
  private projectService = inject(ProjectService);
  projects = toSignal(this.projectService.getProjects());
}
