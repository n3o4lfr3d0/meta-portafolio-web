package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.repository.ProfileRepository;
import com.alfredosoto.portfolio.repository.SkillRepository;
import com.alfredosoto.portfolio.repository.ProjectInfoRepository;
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
                                      EducationRepository educationRepo,
                                      com.alfredosoto.portfolio.repository.LanguageRepository languageRepo,
                                      ProjectInfoRepository projectInfoRepo) {
        return args -> {
            logger.info("Verificando datos iniciales en DynamoDB...");

            // 1. Poblar Perfil (ES y EN)
            seedProfile(profileRepo, "es");
            seedProfile(profileRepo, "en");

            // 2. Poblar Experiencia (Limpieza total y recarga)
            logger.info("Limpiando tabla Experience...");
            experienceRepo.deleteAll();
            
            // 2.1 Sintad
            createExperience(experienceRepo, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Presente", 
                "Me encargo de desarrollar funcionalidades transversales a todos los módulos del software de comercio exterior Sumax. Cada módulo comprende una etapa del ciclo de vida de la importación y exportación de existencias. Mi misión es utilizar las mejores prácticas de programación funcional usando Java para implementar API Rest que se puedan consumir desde mi frontend desarrollado con Angular mediante formularios dinámicos.",
                "https://www.sintad.com.pe", "es");
            
            createExperience(experienceRepo, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Present", 
                "Responsible for developing cross-cutting functionalities for all modules of the Sumax foreign trade software. Each module covers a stage of the import/export lifecycle. My mission is to use best practices in functional programming with Java to implement REST APIs consumed by my Angular frontend using dynamic forms.",
                "https://www.sintad.com.pe", "en");

            // 2.2 Global S1
            createExperience(experienceRepo, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "Encargado de pruebas unitarias y automatizadas de software con Postman, DevTools y Selenium previos a la puesta en Producción. Responsable de redactar los informes de los resultados de los casos de Prueba a los desarrolladores para la corrección de posibles errores. Logros: Detección de bugs críticos antes de puesta en producción y reducción del 15% de carga laboral mediante uso eficiente de Postman.",
                "#", "es");

            createExperience(experienceRepo, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "In charge of unit and automated software testing with Postman, DevTools, and Selenium prior to production deployment. Responsible for drafting test result reports for developers to fix potential errors. Achievements: Detection of critical bugs before production and 15% reduction in workload through efficient use of Postman.",
                "#", "en");

            // 2.3 SG Tech
            createExperience(experienceRepo, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "Encargado de dar soporte directo a las tiendas de la cadena Rústica para el uso y correcto funcionamiento de su software. Reporte de errores al área de desarrollo. Logros: Incremento de eficacia y reducción de carga laboral en un 15% mediante optimización de tiempos de atención.",
                "#", "es");

            createExperience(experienceRepo, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "In charge of providing direct support to Rústica chain stores for the correct use and operation of their software. Reporting errors to the development area. Achievements: Increased efficiency and 15% workload reduction through optimization of service times.",
                "#", "en");

            // 2.5 Poblar Educación (Limpieza total y recarga)
            logger.info("Limpiando tabla Education...");
            educationRepo.deleteAll();
            
            // Cibertec
            createEducation(educationRepo,
                "Computación e Informática",
                "Instituto Superior Tecnológico Cibertec",
                "Jul 2020 - Jul 2023",
                "Formación técnica profesional especializada en desarrollo de software y sistemas de información.",
                "https://www.cibertec.edu.pe", "es");

            createEducation(educationRepo,
                "Computer Science",
                "Cibertec Institute of Technology",
                "Jul 2020 - Jul 2023",
                "Professional technical training specialized in software development and information systems.",
                "https://www.cibertec.edu.pe", "en");

            // 3. Poblar Habilidades (Limpieza total y recarga)
            logger.info("Limpiando tabla Skills...");
            skillRepo.deleteAll();
            
            // Backend
            createSkill(skillRepo, "Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "es");
            createSkill(skillRepo, "Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "en");
            
            createSkill(skillRepo, "Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "es");
            createSkill(skillRepo, "Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "en");
            
            createSkill(skillRepo, "MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "es");
            createSkill(skillRepo, "MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "en");
            
            createSkill(skillRepo, "Microservicios", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "es");
            createSkill(skillRepo, "Microservices", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "en");
            
            // Frontend
            createSkill(skillRepo, "Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "es");
            createSkill(skillRepo, "Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "en");
            
            createSkill(skillRepo, "TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "es");
            createSkill(skillRepo, "TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "en");
            
            createSkill(skillRepo, "JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "es");
            createSkill(skillRepo, "JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "en");
            
            createSkill(skillRepo, "HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "es");
            createSkill(skillRepo, "HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "en");
            
            createSkill(skillRepo, "Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "es");
            createSkill(skillRepo, "Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "en");

            // Cloud & DevOps
            createSkill(skillRepo, "AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "es");
            createSkill(skillRepo, "AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "en");
            
            createSkill(skillRepo, "DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "es");
            createSkill(skillRepo, "DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "en");
            
            createSkill(skillRepo, "Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "es");
            createSkill(skillRepo, "Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "en");
            
            createSkill(skillRepo, "Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "es");
            createSkill(skillRepo, "Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "en");
            
            createSkill(skillRepo, "Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "es");
            createSkill(skillRepo, "Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "en");
            
            createSkill(skillRepo, "Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "es");
            createSkill(skillRepo, "Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "en");

            // QA & Tools
            createSkill(skillRepo, "Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "es");
            createSkill(skillRepo, "Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "en");
            
            createSkill(skillRepo, "Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "es");
            createSkill(skillRepo, "Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "en");
            
            createSkill(skillRepo, "JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "es");
            createSkill(skillRepo, "JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "en");
            
            createSkill(skillRepo, "JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "es");
            createSkill(skillRepo, "JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "en");
            
            createSkill(skillRepo, "Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "es");
            createSkill(skillRepo, "Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "en");

            // Soft Skills
            createSkill(skillRepo, "Metodologías Ágiles", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "es");
            createSkill(skillRepo, "Agile Methodologies", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "en");
            
            createSkill(skillRepo, "Liderazgo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "es");
            createSkill(skillRepo, "Leadership", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "en");
            
            createSkill(skillRepo, "Trabajo en Equipo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "es");
            createSkill(skillRepo, "Teamwork", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "en");
            
            createSkill(skillRepo, "Comunicación Efectiva", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "es");
            createSkill(skillRepo, "Effective Communication", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "en");
            
            createSkill(skillRepo, "Resolución de Problemas", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "es");
            createSkill(skillRepo, "Problem Solving", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "en");
            
            createSkill(skillRepo, "Adaptabilidad", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "es");
            createSkill(skillRepo, "Adaptability", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "en");
            
            createSkill(skillRepo, "Gestión del Tiempo", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "es");
            createSkill(skillRepo, "Time Management", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "en");
            
            createSkill(skillRepo, "Mentoría", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "es");
            createSkill(skillRepo, "Mentoring", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "en");
            
            createSkill(skillRepo, "Inteligencia Emocional", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "es");
            createSkill(skillRepo, "Emotional Intelligence", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "en");

            // 4. Poblar Idiomas
            logger.info("Limpiando tabla Language...");
            languageRepo.deleteAll();

            // ES Content
            createLanguage(languageRepo, "Español", "Nativo", "es", 100, "es");
            createLanguage(languageRepo, "Inglés", "Intermedio (B1/B2)", "en", 60, "es");
            createLanguage(languageRepo, "Portugués", "Básico (A1/A2)", "pt", 20, "es");

            // EN Content
            createLanguage(languageRepo, "Spanish", "Native", "es", 100, "en");
            createLanguage(languageRepo, "English", "Intermediate (B1/B2)", "en", 60, "en");
            createLanguage(languageRepo, "Portuguese", "Basic (A1/A2)", "pt", 20, "en");

            // 5. Poblar Información del Proyecto (Nuevo)
            logger.info("Limpiando tabla ProjectInfo...");
            projectInfoRepo.deleteAll();

            String esTech = """
                - Arquitectura: Hexagonal (Puertos y Adaptadores) y Clean Architecture.
                - Backend: Java 17+, Spring Boot 3.2.3, REST API.
                - Base de Datos: AWS DynamoDB (NoSQL).
                - Frontend: Angular 17+ (Componentes Standalone, Signals), Tailwind CSS.
                - IA: API de Google Gemini (Modelo Flash).
                - Despliegue: Contenedores Docker.
                - Patrones Clave: DTO, Repository, Dependency Injection.
                """;

            String enTech = """
                - Architecture: Hexagonal (Ports and Adapters) & Clean Architecture.
                - Backend: Java 17+, Spring Boot 3.2.3, REST API.
                - Database: AWS DynamoDB (NoSQL).
                - Frontend: Angular 17+ (Standalone Components, Signals), Tailwind CSS.
                - AI: Google Gemini API (Flash Model).
                - Deployment: Docker Containers.
                - Key Patterns: DTO, Repository, Dependency Injection.
                """;

            createProjectInfo(projectInfoRepo, "tech_stack", "Tech Stack", esTech, "es");
            createProjectInfo(projectInfoRepo, "tech_stack", "Tech Stack", enTech, "en");

            logger.info("Poblado de datos completado.");
        };
    }

    private void createProjectInfo(ProjectInfoRepository repo, String id, String category, String content, String lang) {
        com.alfredosoto.portfolio.entity.ProjectInfoEntity info = new com.alfredosoto.portfolio.entity.ProjectInfoEntity();
        info.setId(id);
        info.setCategory(category);
        info.setContent(content);
        info.setLanguage(lang);
        repo.save(info);
    }


    private void createLanguage(com.alfredosoto.portfolio.repository.LanguageRepository repo, String name, String level, String code, Integer percentage, String lang) {
        com.alfredosoto.portfolio.entity.LanguageEntity language = new com.alfredosoto.portfolio.entity.LanguageEntity();
        language.setName(name);
        language.setLevel(level);
        language.setCode(code);
        language.setPercentage(percentage);
        language.setLanguage(lang);
        repo.save(language);
    }

    private void seedProfile(ProfileRepository profileRepo, String lang) {
        String id = "main_" + lang;
        ProfileEntity existingProfile = profileRepo.getProfile(lang);
        
        if (existingProfile == null) {
            logger.info("Poblando perfil para idioma: " + lang);
            ProfileEntity profile = new ProfileEntity();
            profile.setId(id);
            profile.setName("Alfredo Soto");
            setupProfileData(profile, lang);
            profileRepo.save(profile);
        } else {
            logger.info("Actualizando perfil para idioma: " + lang);
            setupProfileData(existingProfile, lang);
            profileRepo.save(existingProfile);
        }
    }

    private void setupProfileData(ProfileEntity profile, String lang) {
        profile.setTitle("Software Developer");
        
        if ("en".equals(lang)) {
            profile.setSummary("I have over 2 years of professional experience as a web developer. The learning process is constant, and my perseverance allows me to contribute important opinions in strategic planning meetings for the start of each development alongside my team.");
            profile.setLocation("Lima, Peru 🇵🇪");
            profile.setExperienceYears("+2 years");
            profile.setSpecialization("Java & Angular");
        } else {
            profile.setSummary("Actualmente tengo más de 2 años trabajando como desarrollador web de forma profesional. El proceso de aprendizaje es constante y mi perseverancia me permite aportar opiniones importantes en las reuniones de planeamiento estratégico para el inicio de cada desarrollo junto con mi equipo de trabajo.");
            profile.setLocation("Lima, Perú 🇵🇪");
            profile.setExperienceYears("+2 años");
            profile.setSpecialization("Java & Angular");
        }
        
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

    private void createExperience(ExperienceRepository repo, String title, String company, String period, String description, String link, String lang) {
        logger.info("Creando experiencia (" + lang + "): " + company);
        ExperienceEntity newExp = new ExperienceEntity();
        newExp.setTitle(title);
        newExp.setCompany(company);
        newExp.setPeriod(period);
        newExp.setDescription(description);
        newExp.setLink(link);
        newExp.setLanguage(lang);
        repo.save(newExp);
    }

    private void createEducation(EducationRepository repo, String degree, String institution, String period, String description, String link, String lang) {
        logger.info("Creando educación (" + lang + "): " + institution);
        EducationEntity newEdu = new EducationEntity();
        newEdu.setDegree(degree);
        newEdu.setInstitution(institution);
        newEdu.setPeriod(period);
        newEdu.setDescription(description);
        newEdu.setLink(link);
        newEdu.setLanguage(lang);
        repo.save(newEdu);
    }

    private void createSkill(SkillRepository repo, String name, String category, Integer level, String icon, String lang) {
        SkillEntity skill = new SkillEntity();
        skill.setName(name);
        skill.setCategory(category);
        skill.setLevel(level);
        skill.setIcon(icon);
        skill.setLanguage(lang);
        repo.save(skill);
    }
}
