package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.repository.ProfileRepository;
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

    private static final String BACKEND_SKILL = "Backend";
    private static final String FRONTEND_SKILL = "Frontend";
    private static final String CLOUD_SKILL = "Cloud";
    private static final String DEVOPS_SKILL = "DevOps";
    private static final String SOFT_SKILLS = "Soft Skills";



    @Bean
    @Order(2)
    public CommandLineRunner seedData(ProfileRepository profileRepo,
                                      ExperienceRepository experienceRepo,
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

            // 2. Poblar Experiencia (Limpieza total y recarga)
            logger.info("Limpiando tabla Experience...");
            List<ExperienceEntity> existingExperiences = experienceRepo.findAll();
            existingExperiences.forEach(experienceRepo::delete);
            
            // 2.1 Sintad
            createExperience(experienceRepo, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Presente", 
                "Me encargo de desarrollar funcionalidades transversales a todos los módulos del software de comercio exterior Sumax. Cada módulo comprende una etapa del ciclo de vida de la importación y exportación de existencias. Mi misión es utilizar las mejores prácticas de programación funcional usando Java para implementar API Rest que se puedan consumir desde mi frontend desarrollado con Angular mediante formularios dinámicos.",
                "https://www.sintad.com.pe");

            // 2.2 Global S1
            createExperience(experienceRepo, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "Encargado de pruebas unitarias y automatizadas de software con Postman, DevTools y Selenium previos a la puesta en Producción. Responsable de redactar los informes de los resultados de los casos de Prueba a los desarrolladores para la corrección de posibles errores. Logros: Detección de bugs críticos antes de puesta en producción y reducción del 15% de carga laboral mediante uso eficiente de Postman.",
                "#");

            // 2.3 SG Tech
            createExperience(experienceRepo, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "Encargado de dar soporte directo a las tiendas de la cadena Rústica para el uso y correcto funcionamiento de su software. Reporte de errores al área de desarrollo. Logros: Incremento de eficacia y reducción de carga laboral en un 15% mediante optimización de tiempos de atención.",
                "#");

            // 2.5 Poblar Educación (Limpieza total y recarga)
            logger.info("Limpiando tabla Education...");
            List<EducationEntity> existingEducation = educationRepo.findAll();
            existingEducation.forEach(educationRepo::delete);
            
            // Cibertec
            createEducation(educationRepo,
                "Computación e Informática",
                "Instituto Superior Tecnológico Cibertec",
                "Jul 2020 - Jul 2023",
                "Formación técnica profesional especializada en desarrollo de software y sistemas de información.",
                "https://www.cibertec.edu.pe");

            // 3. Poblar Habilidades (Limpieza total y recarga)
            logger.info("Limpiando tabla Skills...");
            List<SkillEntity> existingSkills = skillRepo.findAll();
            existingSkills.forEach(skillRepo::delete);
            
            // Backend
            createSkill(skillRepo, "Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg");
            createSkill(skillRepo, "Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg");
            createSkill(skillRepo, "MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg");
            createSkill(skillRepo, "Microservicios", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg");
            
            // Frontend
            createSkill(skillRepo, "Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg");
            createSkill(skillRepo, "TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg");
            createSkill(skillRepo, "JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg");
            createSkill(skillRepo, "HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg");
            createSkill(skillRepo, "Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg");

            // Cloud & DevOps
            createSkill(skillRepo, "AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg");
            createSkill(skillRepo, "DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg");
            createSkill(skillRepo, "Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg");
            createSkill(skillRepo, "Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg");
            createSkill(skillRepo, "Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg");
            createSkill(skillRepo, "Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg");

            // QA & Tools
            createSkill(skillRepo, "Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg");
            createSkill(skillRepo, "Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg");
            createSkill(skillRepo, "JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg");
            createSkill(skillRepo, "JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg");
            createSkill(skillRepo, "Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png");

            // Soft Skills (Separados)
            createSkill(skillRepo, "Scrum", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg");
            createSkill(skillRepo, "Liderazgo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg");
            createSkill(skillRepo, "Trabajo en Equipo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg");
            createSkill(skillRepo, "Comunicación Efectiva", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg");
            createSkill(skillRepo, "Resolución de Problemas", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg");
            createSkill(skillRepo, "Adaptabilidad", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg");
            createSkill(skillRepo, "Gestión del Tiempo", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg");
            createSkill(skillRepo, "Mentoring", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg");
            createSkill(skillRepo, "Inteligencia Emocional", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg");

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

    private void createExperience(ExperienceRepository repo, String title, String company, String period, String description, String link) {
        logger.info("Creando experiencia: " + company);
        ExperienceEntity newExp = new ExperienceEntity();
        newExp.setTitle(title);
        newExp.setCompany(company);
        newExp.setPeriod(period);
        newExp.setDescription(description);
        newExp.setLink(link);
        repo.save(newExp);
    }

    private void createEducation(EducationRepository repo, String degree, String institution, String period, String description, String link) {
        logger.info("Creando educación: " + institution);
        EducationEntity newEdu = new EducationEntity();
        newEdu.setDegree(degree);
        newEdu.setInstitution(institution);
        newEdu.setPeriod(period);
        newEdu.setDescription(description);
        newEdu.setLink(link);
        repo.save(newEdu);
    }

    private void createSkill(SkillRepository repo, String name, String category, Integer level, String icon) {
        SkillEntity skill = new SkillEntity();
        skill.setName(name);
        skill.setCategory(category);
        skill.setLevel(level);
        skill.setIcon(icon);
        repo.save(skill);
    }
}
