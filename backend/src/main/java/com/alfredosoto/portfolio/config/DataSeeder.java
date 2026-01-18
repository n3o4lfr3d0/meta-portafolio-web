package com.alfredosoto.portfolio.config;

import com.alfredosoto.portfolio.entity.EducationEntity;
import com.alfredosoto.portfolio.entity.ExperienceEntity;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
import com.alfredosoto.portfolio.repository.EducationRepository;
import com.alfredosoto.portfolio.repository.ExperienceRepository;
import com.alfredosoto.portfolio.repository.ProfileRepository;
import com.alfredosoto.portfolio.repository.SkillRepository;
import com.alfredosoto.portfolio.repository.ProjectInfoRepository;
import com.alfredosoto.portfolio.repository.LanguageRepository;
import com.alfredosoto.portfolio.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private static final String BACKEND_SKILL = "Backend";
    private static final String FRONTEND_SKILL = "Frontend";
    private static final String CLOUD_SKILL = "Cloud";
    private static final String DEVOPS_SKILL = "DevOps";
    private static final String SOFT_SKILLS = "Soft Skills";

    @org.springframework.beans.factory.annotation.Value("${app.dynamodb.table-suffix:}")
    private String tableSuffix;

    @org.springframework.beans.factory.annotation.Value("${spring.profiles.active:default}")
    private String activeProfile;

    @org.springframework.beans.factory.annotation.Value("${app.dataseed.enabled:true}")
    private boolean dataSeedEnabled;

    private final ProfileRepository profileRepo;
    private final ExperienceRepository experienceRepo;
    private final SkillRepository skillRepo;
    private final EducationRepository educationRepo;
    private final LanguageRepository languageRepo;
    private final ProjectInfoRepository projectInfoRepo;
    private final AuthService authService;

    public DataSeeder(ProfileRepository profileRepo,
                      ExperienceRepository experienceRepo,
                      SkillRepository skillRepo,
                      EducationRepository educationRepo,
                      LanguageRepository languageRepo,
                      ProjectInfoRepository projectInfoRepo,
                      AuthService authService) {
        this.profileRepo = profileRepo;
        this.experienceRepo = experienceRepo;
        this.skillRepo = skillRepo;
        this.educationRepo = educationRepo;
        this.languageRepo = languageRepo;
        this.projectInfoRepo = projectInfoRepo;
        this.authService = authService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 1. Global Check: If disabled via config/env, stop immediately.
        if (!dataSeedEnabled) {
            logger.info("🛑 DataSeeder DESHABILITADO por configuración (app.dataseed.enabled=false). Perfil: '{}'", activeProfile);
            return;
        }

        // SAFETY CHECK: Prevent accidental wiping of production tables
        boolean isProd = "prod".equalsIgnoreCase(activeProfile) || "production".equalsIgnoreCase(activeProfile);

        if ((tableSuffix == null || tableSuffix.trim().isEmpty()) && !"local".equalsIgnoreCase(activeProfile)) {
            // Allow prod seed ONLY if explicitly enabled (redundant check but safe)
            if (isProd && dataSeedEnabled) {
                 logger.warn("⚠️ ALERTA: DataSeeder habilitado explícitamente en PRODUCCIÓN. Se procederá a borrar y recargar datos.");
            } else {
                logger.warn("🛑 SEGURIDAD: DataSeeder ABORTADO. Se detectó sufijo de tabla vacío en entorno '{}'. Configure DYNAMODB_TABLE_SUFFIX o DATA_SEED_ENABLED=true para ejecutar.", activeProfile);
                return;
            }
        }
        
        // Run in a separate thread to avoid blocking the main thread if possible, 
        // although ApplicationReadyEvent is already late enough.
        // We run directly but with try-catch to ensure app stays alive.
        seedData();
    }

    private void seedData() {
        logger.info("Verificando datos iniciales en DynamoDB (Sufijo: '{}')...", tableSuffix);

        try {
            // 0. Seed Admin User
            authService.createAdminUserIfNotFound();

            // 1. Poblar Perfil (ES y EN)
            seedProfile(profileRepo, "es");
            seedProfile(profileRepo, "en");

            // 2. Poblar Experiencia (Upsert - Idempotente)
            logger.info("Actualizando tabla Experience...");
            // experienceRepo.deleteAll(); // REMOVED to prevent OOM on large tables
            
            // 2.1 Sintad
            saveExperience(experienceRepo, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Presente", 
                "Me encargo de desarrollar funcionalidades transversales a todos los módulos del software de comercio exterior Sumax. Cada módulo comprende una etapa del ciclo de vida de la importación y exportación de existencias. Mi misión es utilizar las mejores prácticas de programación funcional usando Java para implementar API Rest que se puedan consumir desde mi frontend desarrollado con Angular mediante formularios dinámicos.",
                "https://www.sintad.com.pe", "es");
            
            saveExperience(experienceRepo, 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Present", 
                "Responsible for developing cross-cutting functionalities for all modules of the Sumax foreign trade software. Each module covers a stage of the import/export lifecycle. My mission is to use best practices in functional programming with Java to implement REST APIs consumed by my Angular frontend using dynamic forms.",
                "https://www.sintad.com.pe", "en");

            // 2.2 Global S1
            saveExperience(experienceRepo, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "Encargado de pruebas unitarias y automatizadas de software con Postman, DevTools y Selenium previos a la puesta en Producción. Responsable de redactar los informes de los resultados de los casos de Prueba a los desarrolladores para la corrección de posibles errores. Logros: Detección de bugs críticos antes de puesta en producción y reducción del 15% de carga laboral mediante uso eficiente de Postman.",
                "#", "es");

            saveExperience(experienceRepo, 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "In charge of unit and automated software testing with Postman, DevTools, and Selenium prior to production deployment. Responsible for drafting test result reports for developers to fix potential errors. Achievements: Detection of critical bugs before production and 15% reduction in workload through efficient use of Postman.",
                "#", "en");

            // 2.3 SG Tech
            saveExperience(experienceRepo, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "Encargado de dar soporte directo a las tiendas de la cadena Rústica para el uso y correcto funcionamiento de su software. Reporte de errores al área de desarrollo. Logros: Incremento de eficacia y reducción de carga laboral en un 15% mediante optimización de tiempos de atención.",
                "#", "es");

            saveExperience(experienceRepo, 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "In charge of providing direct support to Rústica chain stores for the correct use and operation of their software. Reporting errors to the development area. Achievements: Increased efficiency and 15% workload reduction through optimization of service times.",
                "#", "en");

            // 2.5 Poblar Educación (Upsert - Idempotente)
            logger.info("Actualizando tabla Education...");
            // educationRepo.deleteAll(); // REMOVED
            
            // Cibertec
            saveEducation(educationRepo,
                "Computación e Informática",
                "Instituto Superior Tecnológico Cibertec",
                "Jul 2020 - Jul 2023",
                "Formación técnica profesional especializada en desarrollo de software y sistemas de información.",
                "https://www.cibertec.edu.pe", "es");

            saveEducation(educationRepo,
                "Computer Science",
                "Cibertec Institute of Technology",
                "Jul 2020 - Jul 2023",
                "Professional technical training specialized in software development and information systems.",
                "https://www.cibertec.edu.pe", "en");

            // 3. Poblar Habilidades (Upsert - Idempotente)
            logger.info("Actualizando tabla Skills...");
            // skillRepo.deleteAll(); // REMOVED
            
            // Backend
            saveSkill(skillRepo, "Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "es");
            saveSkill(skillRepo, "Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "en");
            
            saveSkill(skillRepo, "Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "es");
            saveSkill(skillRepo, "Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "en");
            
            saveSkill(skillRepo, "MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "es");
            saveSkill(skillRepo, "MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "en");
            
            saveSkill(skillRepo, "Microservicios", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "es");
            saveSkill(skillRepo, "Microservices", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "en");
            
            // Frontend
            saveSkill(skillRepo, "Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "es");
            saveSkill(skillRepo, "Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "en");
            
            saveSkill(skillRepo, "TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "es");
            saveSkill(skillRepo, "TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "en");
            
            saveSkill(skillRepo, "JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "es");
            saveSkill(skillRepo, "JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "en");
            
            saveSkill(skillRepo, "HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "es");
            saveSkill(skillRepo, "HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "en");
            
            saveSkill(skillRepo, "Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "es");
            saveSkill(skillRepo, "Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "en");

            // Cloud & DevOps
            saveSkill(skillRepo, "AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "es");
            saveSkill(skillRepo, "AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "en");
            
            saveSkill(skillRepo, "DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "es");
            saveSkill(skillRepo, "DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "en");
            
            saveSkill(skillRepo, "Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "es");
            saveSkill(skillRepo, "Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "en");
            
            saveSkill(skillRepo, "Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "es");
            saveSkill(skillRepo, "Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "en");
            
            saveSkill(skillRepo, "Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "es");
            saveSkill(skillRepo, "Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "en");
            
            saveSkill(skillRepo, "Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "es");
            saveSkill(skillRepo, "Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "en");

            // QA & Tools
            saveSkill(skillRepo, "Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "es");
            saveSkill(skillRepo, "Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "en");
            
            saveSkill(skillRepo, "Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "es");
            saveSkill(skillRepo, "Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "en");
            
            saveSkill(skillRepo, "JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "es");
            saveSkill(skillRepo, "JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "en");
            
            saveSkill(skillRepo, "JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "es");
            saveSkill(skillRepo, "JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "en");
            
            saveSkill(skillRepo, "Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "es");
            saveSkill(skillRepo, "Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "en");

            // Soft Skills
            saveSkill(skillRepo, "Metodologías Ágiles", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "es");
            saveSkill(skillRepo, "Agile Methodologies", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "en");
            
            saveSkill(skillRepo, "Liderazgo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "es");
            saveSkill(skillRepo, "Leadership", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "en");
            
            saveSkill(skillRepo, "Trabajo en Equipo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "es");
            saveSkill(skillRepo, "Teamwork", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "en");
            
            saveSkill(skillRepo, "Comunicación Efectiva", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "es");
            saveSkill(skillRepo, "Effective Communication", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "en");
            
            saveSkill(skillRepo, "Resolución de Problemas", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "es");
            saveSkill(skillRepo, "Problem Solving", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "en");
            
            saveSkill(skillRepo, "Adaptabilidad", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "es");
            saveSkill(skillRepo, "Adaptability", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "en");
            
            saveSkill(skillRepo, "Gestión del Tiempo", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "es");
            saveSkill(skillRepo, "Time Management", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "en");
            
            saveSkill(skillRepo, "Mentoría", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "es");
            saveSkill(skillRepo, "Mentoring", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "en");
            
            saveSkill(skillRepo, "Inteligencia Emocional", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "es");
            saveSkill(skillRepo, "Emotional Intelligence", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "en");
            
            // 4. Poblar Idiomas
            logger.info("Actualizando tabla Language...");
            // languageRepo.deleteAll(); // REMOVED

            // ES Content
            saveLanguage(languageRepo, "Español", "Nativo", "es", 100, "es");
            saveLanguage(languageRepo, "Inglés", "Intermedio (B1/B2)", "en", 60, "es");
            saveLanguage(languageRepo, "Portugués", "Básico (A1/A2)", "pt", 20, "es");

            // EN Content
            saveLanguage(languageRepo, "Spanish", "Native", "es", 100, "en");
            saveLanguage(languageRepo, "English", "Intermediate (B1/B2)", "en", 60, "en");
            saveLanguage(languageRepo, "Portuguese", "Basic (A1/A2)", "pt", 20, "en");
            
            // 5. Poblar Información del Proyecto (Nuevo)
            logger.info("Actualizando tabla ProjectInfo...");
            // projectInfoRepo.deleteAll(); // REMOVED

            String esTech = """
                - Arquitectura: Hexagonal (Puertos y Adaptadores) y Clean Architecture.
                - Backend: Java 21, Spring Boot 3, DynamoDB, AWS SDK v2, Spring Security (JWT).
                - Frontend: Angular 17, Tailwind CSS, Componentes Standalone.
                - DevOps: Docker, GitHub Actions, Railway (Backend/BD), Vercel (Frontend).
                - Testing: JUnit 5, Mockito.
                """;

            String enTech = """
                - Architecture: Hexagonal (Ports and Adapters) and Clean Architecture.
                - Backend: Java 21, Spring Boot 3, DynamoDB, AWS SDK v2, Spring Security (JWT).
                - Frontend: Angular 17, Tailwind CSS, Standalone Components.
                - DevOps: Docker, GitHub Actions, Railway (Backend/DB), Vercel (Frontend).
                - Testing: JUnit 5, Mockito.
                """;

            String esFeat = """
                - API REST reactiva y segura con Spring Boot.
                - Base de datos NoSQL DynamoDB optimizada para alta concurrencia.
                - Frontend moderno y responsivo con Angular 17.
                - Panel administrativo seguro para gestión de contenido.
                - Despliegue continuo (CI/CD) automatizado.
                """;

            String enFeat = """
                - Reactive and secure REST API with Spring Boot.
                - NoSQL DynamoDB database optimized for high concurrency.
                - Modern and responsive frontend with Angular 17.
                - Secure administrative panel for content management.
                - Automated Continuous Deployment (CI/CD).
                """;

            saveProjectInfo(projectInfoRepo, "Tecnologías", esTech, "es");
            saveProjectInfo(projectInfoRepo, "Technologies", enTech, "en");
            saveProjectInfo(projectInfoRepo, "Características Destacadas", esFeat, "es");
            saveProjectInfo(projectInfoRepo, "Key Features", enFeat, "en");

            logger.info("✅ DataSeeder completado exitosamente.");

        } catch (Throwable e) {
            logger.error("❌ ERROR CRÍTICO en DataSeeder: {}", e.getMessage(), e);
            // No re-lanzamos la excepción para evitar detener la aplicación si ya está corriendo
        }
    }

    private void seedProfile(ProfileRepository repo, String lang) {
        if ("en".equals(lang)) {
            ProfileEntity profile = new ProfileEntity();
            profile.setId("main_en"); // Fixed ID compatible with ProfileRepository.getProfile("en")
            profile.setLanguage("en");
            profile.setName("Alfredo Soto Nolazco");
            profile.setTitle("Fullstack Developer | Java | Spring Boot | Angular | AWS");
            profile.setSummary("Systems Engineer with experience in software development using Java, Spring Boot, Angular, and AWS. Passionate about clean architecture, good practices, and continuous learning. Focused on creating scalable and efficient solutions.");
            profile.setPhotoUrl("https://avatars.githubusercontent.com/u/108922650?v=4"); // Replace with real URL
            profile.setCvUrl("/assets/cv-alfredo-soto-en.pdf"); // Local path or cloud URL
            profile.setGithubUrl("https://github.com/n3o4lfr3d0");
            profile.setLinkedinUrl("https://www.linkedin.com/in/alfredosoto");
            profile.setEmail("alfredo.soton@gmail.com");
            repo.save(profile);
        } else {
            ProfileEntity profile = new ProfileEntity();
            profile.setId("main_es"); // Fixed ID compatible with ProfileRepository.getProfile("es")
            profile.setLanguage("es");
            profile.setName("Alfredo Soto Nolazco");
            profile.setTitle("Desarrollador Fullstack | Java | Spring Boot | Angular | AWS");
            profile.setSummary("Ingeniero de Sistemas con experiencia en desarrollo de software utilizando Java, Spring Boot, Angular y AWS. Apasionado por la arquitectura limpia, buenas prácticas y aprendizaje continuo. Enfocado en crear soluciones escalables y eficientes.");
            profile.setPhotoUrl("https://avatars.githubusercontent.com/u/108922650?v=4"); // Reemplazar con URL real
            profile.setCvUrl("/assets/cv-alfredo-soto.pdf"); // Ruta local o URL nube
            profile.setGithubUrl("https://github.com/n3o4lfr3d0");
            profile.setLinkedinUrl("https://www.linkedin.com/in/alfredosoto");
            profile.setEmail("alfredo.soton@gmail.com");
            repo.save(profile);
        }
    }

    private void saveExperience(ExperienceRepository repo, String position, String company, String period, String description, String url, String lang) {
        ExperienceEntity exp = new ExperienceEntity();
        // Deterministic ID to allow upserts and avoid duplicates
        String uniqueKey = "experience_" + company.trim() + "_" + position.trim() + "_" + lang;
        exp.setId(UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString());
        
        exp.setTitle(position);
        exp.setCompany(company);
        exp.setPeriod(period);
        exp.setDescription(description);
        exp.setLink(url);
        exp.setLanguage(lang);
        repo.save(exp);
    }

    private void saveSkill(SkillRepository repo, String name, String category, int level, String iconUrl, String lang) {
        SkillEntity skill = new SkillEntity();
        // Deterministic ID
        String uniqueKey = "skill_" + category.trim() + "_" + name.trim() + "_" + lang;
        skill.setId(UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString());
        
        skill.setName(name);
        skill.setCategory(category);
        skill.setLevel(level);
        skill.setIcon(iconUrl);
        skill.setLanguage(lang);
        repo.save(skill);
    }

    private void saveEducation(EducationRepository repo, String degree, String institution, String period, String description, String url, String lang) {
        EducationEntity edu = new EducationEntity();
        // Deterministic ID
        String uniqueKey = "education_" + institution.trim() + "_" + degree.trim() + "_" + lang;
        edu.setId(UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString());
        
        edu.setDegree(degree);
        edu.setInstitution(institution);
        edu.setPeriod(period);
        edu.setDescription(description);
        edu.setLink(url);
        edu.setLanguage(lang);
        repo.save(edu);
    }
    
    private void saveLanguage(LanguageRepository repo, String name, String level, String code, int percentage, String lang) {
        LanguageEntity l = new LanguageEntity();
        // Deterministic ID
        String uniqueKey = "language_" + code.trim() + "_" + name.trim() + "_" + lang;
        l.setId(UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString());
        
        l.setName(name);
        l.setLevel(level);
        l.setCode(code);
        l.setPercentage(percentage);
        l.setLanguage(lang);
        repo.save(l);
    }

    private void saveProjectInfo(ProjectInfoRepository repo, String title, String content, String lang) {
        ProjectInfoEntity p = new ProjectInfoEntity();
        // Deterministic ID
        String uniqueKey = "project_" + title.trim() + "_" + lang;
        p.setId(UUID.nameUUIDFromBytes(uniqueKey.getBytes()).toString());
        
        p.setCategory(title);
        p.setContent(content);
        p.setLanguage(lang);
        repo.save(p);
    }
}
