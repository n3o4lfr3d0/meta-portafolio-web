package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.entity.ProjectEntity;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.repository.ProfileRepository;
import com.alfredosoto.portfolio.repository.ProjectRepository;
import com.alfredosoto.portfolio.repository.SkillRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Bean
    @Order(2)
    public CommandLineRunner seedData(ProfileRepository profileRepo,
                                      ExperienceRepository experienceRepo,
                                      ProjectRepository projectRepo,
                                      SkillRepository skillRepo,
                                      EducationRepository educationRepo) {
        return args -> {
            logger.info("Verificando datos iniciales en DynamoDB...");

            // 1. Poblar Perfil
            ProfileEntity existingProfile = profileRepo.getProfile();
            if (existingProfile == null) {
                logger.info("Poblando tabla Profile...");
                ProfileEntity profile = new ProfileEntity();
                profile.setId("main");
                profile.setName("Alfredo Soto");
                setupProfileData(profile);
                profileRepo.save(profile);
            } else {
                logger.info("Actualizando perfil con datos del CV...");
                setupProfileData(existingProfile);
                profileRepo.save(existingProfile);
            }

            // 2. Poblar Experiencia (Limpiar y recargar para coincidir con CV)
            List<ExperienceEntity> currentExperiences = experienceRepo.findAll();
            
            // 2.1 Sintad
            upsertExperience(experienceRepo, currentExperiences, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Presente", 
                "Me encargo de desarrollar funcionalidades transversales a todos los módulos del software de comercio exterior Sumax. Cada módulo comprende una etapa del ciclo de vida de la importación y exportación de existencias. Mi misión es utilizar las mejores prácticas de programación funcional usando Java para implementar API Rest que se puedan consumir desde mi frontend desarrollado con Angular mediante formularios dinámicos.",
                "https://www.sintad.com.pe");

            // 2.2 Global S1
            upsertExperience(experienceRepo, currentExperiences, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "Encargado de pruebas unitarias y automatizadas de software con Postman, DevTools y Selenium previos a la puesta en Producción. Responsable de redactar los informes de los resultados de los casos de Prueba a los desarrolladores para la corrección de posibles errores. Logros: Detección de bugs críticos antes de puesta en producción y reducción del 15% de carga laboral mediante uso eficiente de Postman.",
                "#");

            // 2.3 SG Tech
            upsertExperience(experienceRepo, currentExperiences, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "Encargado de dar soporte directo a las tiendas de la cadena Rústica para el uso y correcto funcionamiento de su software. Reporte de errores al área de desarrollo. Logros: Incremento de eficacia y reducción de carga laboral en un 15% mediante optimización de tiempos de atención.",
                "#");

            // Limpiar Cibertec de experiencia si existe (ya que lo movemos a educación)
            removeExperience(experienceRepo, currentExperiences, "Cibertec");
            removeExperience(experienceRepo, currentExperiences, "Computación e Informática");
            removeExperience(experienceRepo, currentExperiences, "Freelance");
            removeExperience(experienceRepo, currentExperiences, "Universidad");

            // 2.5 Poblar Educación
            List<EducationEntity> currentEducation = educationRepo.findAll();
            
            // Cibertec
            upsertEducation(educationRepo, currentEducation,
                "Computación e Informática",
                "Instituto Superior Tecnológico Cibertec",
                "Jul 2020 - Jul 2023",
                "Formación técnica profesional especializada en desarrollo de software y sistemas de información.",
                "https://www.cibertec.edu.pe");

            // 3. Poblar Proyectos
            if (projectRepo.findAll().isEmpty()) {
                logger.info("Poblando tabla Projects...");

                ProjectEntity p1 = new ProjectEntity();
                p1.setTitle("Sistema de Gestión Aduanera");
                p1.setDescription("Plataforma integral para la gestión de trámites aduaneros, optimizando tiempos de respuesta y trazabilidad.");
                p1.setTags(List.of("Angular", "Spring Boot", "Oracle", "Docker"));
                p1.setImage("/projects/aduanas.svg");
                p1.setGithubLink("https://github.com/alfredosoto/aduanas"); // Placeholder
                p1.setDemoLink("https://aduanas-demo.com"); // Placeholder
                projectRepo.save(p1);

                ProjectEntity p2 = new ProjectEntity();
                p2.setTitle("Portfolio Personal");
                p2.setDescription("Este mismo portafolio, construido con arquitectura limpia, AWS DynamoDB y las últimas tecnologías web.");
                p2.setTags(List.of("Angular 17", "Spring Boot 3", "DynamoDB", "Tailwind CSS"));
                p2.setImage("/projects/portfolio.svg");
                p2.setGithubLink("https://github.com/alfredosoto/portfolio");
                p2.setDemoLink("https://alfredosoto.dev");
                projectRepo.save(p2);
            }

            // 4. Poblar Habilidades (Actualizar desde CV)
            List<SkillEntity> currentSkills = skillRepo.findAll();
            
            // Backend
            upsertSkill(skillRepo, currentSkills, "Java", "Backend", 95, "java");
            upsertSkill(skillRepo, currentSkills, "Spring Boot", "Backend", 90, "spring");
            upsertSkill(skillRepo, currentSkills, "MySQL", "Backend", 85, "mysql");
            upsertSkill(skillRepo, currentSkills, "Microservicios", "Backend", 85, "server");
            
            // Frontend
            upsertSkill(skillRepo, currentSkills, "Angular", "Frontend", 95, "angular");
            upsertSkill(skillRepo, currentSkills, "TypeScript", "Frontend", 90, "typescript");
            upsertSkill(skillRepo, currentSkills, "JavaScript", "Frontend", 90, "javascript");
            upsertSkill(skillRepo, currentSkills, "HTML/CSS", "Frontend", 95, "html");
            upsertSkill(skillRepo, currentSkills, "Tailwind CSS", "Frontend", 90, "tailwind");

            // Cloud & DevOps
            upsertSkill(skillRepo, currentSkills, "AWS Cloud", "Cloud", 80, "aws");
            upsertSkill(skillRepo, currentSkills, "Google Cloud", "Cloud", 70, "google");
            upsertSkill(skillRepo, currentSkills, "Docker", "DevOps", 75, "docker");
            upsertSkill(skillRepo, currentSkills, "Git", "DevOps", 90, "git");
            upsertSkill(skillRepo, currentSkills, "Linux", "DevOps", 70, "linux");

            // QA & Tools
            upsertSkill(skillRepo, currentSkills, "Postman", "Tools", 95, "postman");
            upsertSkill(skillRepo, currentSkills, "Selenium", "QA", 80, "selenium");
            upsertSkill(skillRepo, currentSkills, "JMeter", "QA", 75, "speed");

            // Soft Skills
            upsertSkill(skillRepo, currentSkills, "Scrum", "Methodology", 90, "agile");
            upsertSkill(skillRepo, currentSkills, "Liderazgo", "Soft Skills", 100, "users");
            upsertSkill(skillRepo, currentSkills, "Trabajo en Equipo", "Soft Skills", 100, "users");

            logger.info("Poblado de datos completado.");
        };
    }

    private void setupProfileData(ProfileEntity profile) {
        profile.setTitle("Software Developer");
        profile.setSummary("Actualmente tengo más de 2 años trabajando como desarrollador web de forma profesional. El proceso de aprendizaje es constante y mi perseverancia me permite aportar opiniones importantes en las reuniones de planeamiento estratégico para el inicio de cada desarrollo junto con mi equipo de trabajo.");
        profile.setLocation("Lima, Perú 🇵🇪");
        profile.setExperienceYears("+2 años");
        profile.setSpecialization("Java & Angular");
        
        ProfileEntity.SocialLinkEntity linkedin = new ProfileEntity.SocialLinkEntity();
        linkedin.setName("LinkedIn");
        linkedin.setUrl("https://www.linkedin.com/in/alfredo-soto-nolazco/");
        linkedin.setIcon("linkedin");
        
        ProfileEntity.SocialLinkEntity email = new ProfileEntity.SocialLinkEntity();
        email.setName("Email");
        email.setUrl("mailto:alfredosotonolazco@gmail.com");
        email.setIcon("mail");
        
        ProfileEntity.SocialLinkEntity github = new ProfileEntity.SocialLinkEntity();
        github.setName("GitHub");
        github.setUrl("https://github.com/n3o4lfr3d0");
        github.setIcon("github");

        profile.setSocialLinks(List.of(linkedin, email, github));
    }

    private void upsertExperience(ExperienceRepository repo, List<ExperienceEntity> current, String title, String company, String period, String description, String link) {
        ExperienceEntity existing = current.stream()
            .filter(e -> e.getCompany().equalsIgnoreCase(company))
            .findFirst()
            .orElse(null);

        if (existing != null) {
            logger.info("Actualizando experiencia: " + company);
            existing.setTitle(title);
            existing.setPeriod(period);
            existing.setDescription(description);
            existing.setLink(link);
            repo.save(existing);
        } else {
            logger.info("Creando experiencia: " + company);
            ExperienceEntity newExp = new ExperienceEntity();
            newExp.setTitle(title);
            newExp.setCompany(company);
            newExp.setPeriod(period);
            newExp.setDescription(description);
            newExp.setLink(link);
            repo.save(newExp);
        }
    }

    private void removeExperience(ExperienceRepository repo, List<ExperienceEntity> current, String keyword) {
        current.stream()
            .filter(e -> e.getTitle().contains(keyword) || e.getCompany().contains(keyword))
            .forEach(e -> {
                logger.info("Eliminando experiencia obsoleta: " + e.getCompany());
                repo.delete(e);
            });
    }

    private void upsertEducation(EducationRepository repo, List<EducationEntity> current, String degree, String institution, String period, String description, String link) {
        EducationEntity existing = current.stream()
            .filter(e -> e.getInstitution().equalsIgnoreCase(institution))
            .findFirst()
            .orElse(null);

        if (existing != null) {
            logger.info("Actualizando educación: " + institution);
            existing.setDegree(degree);
            existing.setPeriod(period);
            existing.setDescription(description);
            existing.setLink(link);
            repo.save(existing);
        } else {
            logger.info("Creando educación: " + institution);
            EducationEntity newEdu = new EducationEntity();
            newEdu.setDegree(degree);
            newEdu.setInstitution(institution);
            newEdu.setPeriod(period);
            newEdu.setDescription(description);
            newEdu.setLink(link);
            repo.save(newEdu);
        }
    }

    private void upsertSkill(SkillRepository repo, List<SkillEntity> current, String name, String category, Integer level, String icon) {
        boolean exists = current.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name));
        if (!exists) {
            SkillEntity skill = new SkillEntity();
            skill.setName(name);
            skill.setCategory(category);
            skill.setLevel(level);
            skill.setIcon(icon);
            repo.save(skill);
        }
    }
}
