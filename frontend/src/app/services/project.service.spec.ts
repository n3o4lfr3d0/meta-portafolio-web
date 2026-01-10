import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { ProjectService } from './project.service';
import { Project } from '../models/project.model';

describe('ProjectService', () => {
  let service: ProjectService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ProjectService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(ProjectService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve projects', () => {
    const dummyProjects: Project[] = [
      { title: 'Project 1', description: 'Desc', tags: ['Java'], image: '', githubLink: '', demoLink: '' }
    ];

    service.getProjects().subscribe(projects => {
      expect(projects.length).toBe(1);
      expect(projects[0].title).toBe('Project 1');
    });

    const req = httpMock.expectOne('http://localhost:8080/api/projects');
    expect(req.request.method).toBe('GET');
    req.flush(dummyProjects);
  });
});
