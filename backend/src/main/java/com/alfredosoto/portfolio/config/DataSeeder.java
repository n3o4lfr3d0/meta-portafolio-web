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

import java.util.ArrayList;
import java.util.List;
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

            // 2. Poblar Experiencia (Limpieza total y recarga)
            logger.info("Limpiando tabla Experience...");
            experienceRepo.deleteAll();
            List<ExperienceEntity> experiences = new ArrayList<>();
            
            // 2.1 Sintad
            experiences.add(createExperience( 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Presente", 
                "Me encargo de desarrollar funcionalidades transversales a todos los módulos del software de comercio exterior Sumax. Cada módulo comprende una etapa del ciclo de vida de la importación y exportación de existencias. Mi misión es utilizar las mejores prácticas de programación funcional usando Java para implementar API Rest que se puedan consumir desde mi frontend desarrollado con Angular mediante formularios dinámicos.",
                "https://www.sintad.com.pe", "es"));
            
            experiences.add(createExperience( 
                "Fullstack Developer", 
                "Sintad", 
                "Sep 2023 - Present", 
                "Responsible for developing cross-cutting functionalities for all modules of the Sumax foreign trade software. Each module covers a stage of the import/export lifecycle. My mission is to use best practices in functional programming with Java to implement REST APIs consumed by my Angular frontend using dynamic forms.",
                "https://www.sintad.com.pe", "en"));

            // 2.2 Global S1
            experiences.add(createExperience( 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "Encargado de pruebas unitarias y automatizadas de software con Postman, DevTools y Selenium previos a la puesta en Producción. Responsable de redactar los informes de los resultados de los casos de Prueba a los desarrolladores para la corrección de posibles errores. Logros: Detección de bugs críticos antes de puesta en producción y reducción del 15% de carga laboral mediante uso eficiente de Postman.",
                "#", "es"));

            experiences.add(createExperience( 
                "Quality Manager", 
                "Global S1", 
                "Jun 2023 - Jul 2023", 
                "In charge of unit and automated software testing with Postman, DevTools, and Selenium prior to production deployment. Responsible for drafting test result reports for developers to fix potential errors. Achievements: Detection of critical bugs before production and 15% reduction in workload through efficient use of Postman.",
                "#", "en"));

            // 2.3 SG Tech
            experiences.add(createExperience( 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "Encargado de dar soporte directo a las tiendas de la cadena Rústica para el uso y correcto funcionamiento de su software. Reporte de errores al área de desarrollo. Logros: Incremento de eficacia y reducción de carga laboral en un 15% mediante optimización de tiempos de atención.",
                "#", "es"));

            experiences.add(createExperience( 
                "Help Desk", 
                "SG Tech", 
                "Nov 2022 - Mar 2023", 
                "In charge of providing direct support to Rústica chain stores for the correct use and operation of their software. Reporting errors to the development area. Achievements: Increased efficiency and 15% workload reduction through optimization of service times.",
                "#", "en"));

            experienceRepo.saveAll(experiences);
            
            // 2.5 Poblar Educación (Limpieza total y recarga)
            logger.info("Limpiando tabla Education...");
            educationRepo.deleteAll();
            List<EducationEntity> educationList = new ArrayList<>();
            
            // Cibertec
            educationList.add(createEducation(
                "Computación e Informática",
                "Instituto Superior Tecnológico Cibertec",
                "Jul 2020 - Jul 2023",
                "Formación técnica profesional especializada en desarrollo de software y sistemas de información.",
                "https://www.cibertec.edu.pe", "es"));

            educationList.add(createEducation(
                "Computer Science",
                "Cibertec Institute of Technology",
                "Jul 2020 - Jul 2023",
                "Professional technical training specialized in software development and information systems.",
                "https://www.cibertec.edu.pe", "en"));

            educationRepo.saveAll(educationList);

            // 3. Poblar Habilidades (Limpieza total y recarga)
            logger.info("Limpiando tabla Skills...");
            skillRepo.deleteAll();
            List<SkillEntity> skills = new ArrayList<>();
            
            // Backend
            skills.add(createSkill("Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "es"));
            skills.add(createSkill("Java", BACKEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg", "en"));
            
            skills.add(createSkill("Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "es"));
            skills.add(createSkill("Spring Boot", BACKEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original.svg", "en"));
            
            skills.add(createSkill("MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "es"));
            skills.add(createSkill("MySQL", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/mysql/mysql-original.svg", "en"));
            
            skills.add(createSkill("Microservicios", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "es"));
            skills.add(createSkill("Microservices", BACKEND_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/spring/spring-original-wordmark.svg", "en"));
            
            // Frontend
            skills.add(createSkill("Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "es"));
            skills.add(createSkill("Angular", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/angular/angular-original.svg", "en"));
            
            skills.add(createSkill("TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "es"));
            skills.add(createSkill("TypeScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/typescript/typescript-original.svg", "en"));
            
            skills.add(createSkill("JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "es"));
            skills.add(createSkill("JavaScript", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/javascript/javascript-original.svg", "en"));
            
            skills.add(createSkill("HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "es"));
            skills.add(createSkill("HTML/CSS", FRONTEND_SKILL, 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/html5/html5-original.svg", "en"));
            
            skills.add(createSkill("Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "es"));
            skills.add(createSkill("Tailwind CSS", FRONTEND_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/tailwindcss/tailwindcss-original.svg", "en"));

            // Cloud & DevOps
            skills.add(createSkill("AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "es"));
            skills.add(createSkill("AWS Cloud", CLOUD_SKILL, 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg", "en"));
            
            skills.add(createSkill("DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "es"));
            skills.add(createSkill("DynamoDB", CLOUD_SKILL, 85, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/dynamodb/dynamodb-original.svg", "en"));
            
            skills.add(createSkill("Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "es"));
            skills.add(createSkill("Google Cloud", CLOUD_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/googlecloud/googlecloud-original.svg", "en"));
            
            skills.add(createSkill("Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "es"));
            skills.add(createSkill("Docker", DEVOPS_SKILL, 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/docker/docker-original.svg", "en"));
            
            skills.add(createSkill("Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "es"));
            skills.add(createSkill("Git", DEVOPS_SKILL, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/git/git-original.svg", "en"));
            
            skills.add(createSkill("Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "es"));
            skills.add(createSkill("Linux", DEVOPS_SKILL, 70, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/linux/linux-original.svg", "en"));

            // QA & Tools
            skills.add(createSkill("Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "es"));
            skills.add(createSkill("Postman", "Tools", 95, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg", "en"));
            
            skills.add(createSkill("Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "es"));
            skills.add(createSkill("Selenium", "QA", 80, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/selenium/selenium-original.svg", "en"));
            
            skills.add(createSkill("JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "es"));
            skills.add(createSkill("JMeter", "QA", 75, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/apache/apache-original.svg", "en"));
            
            skills.add(createSkill("JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "es"));
            skills.add(createSkill("JUnit", "QA", 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/junit/junit-original.svg", "en"));
            
            skills.add(createSkill("Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "es"));
            skills.add(createSkill("Mockito", "QA", 85, "https://github.com/mockito/mockito.github.io/raw/master/img/logo%402x.png", "en"));

            // Soft Skills
            skills.add(createSkill("Metodologías Ágiles", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "es"));
            skills.add(createSkill("Agile Methodologies", SOFT_SKILLS, 90, "https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/jira/jira-original.svg", "en"));
            
            skills.add(createSkill("Liderazgo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "es"));
            skills.add(createSkill("Leadership", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/manager.svg", "en"));
            
            skills.add(createSkill("Trabajo en Equipo", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "es"));
            skills.add(createSkill("Teamwork", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/conference-call.svg", "en"));
            
            skills.add(createSkill("Comunicación Efectiva", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "es"));
            skills.add(createSkill("Effective Communication", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/comments.svg", "en"));
            
            skills.add(createSkill("Resolución de Problemas", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "es"));
            skills.add(createSkill("Problem Solving", SOFT_SKILLS, 100, "https://api.iconify.design/flat-color-icons/puzzle.svg", "en"));
            
            skills.add(createSkill("Adaptabilidad", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "es"));
            skills.add(createSkill("Adaptability", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/process.svg", "en"));
            
            skills.add(createSkill("Gestión del Tiempo", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "es"));
            skills.add(createSkill("Time Management", SOFT_SKILLS, 85, "https://api.iconify.design/flat-color-icons/clock.svg", "en"));
            
            skills.add(createSkill("Mentoría", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "es"));
            skills.add(createSkill("Mentoring", SOFT_SKILLS, 90, "https://api.iconify.design/flat-color-icons/reading.svg", "en"));
            
            skills.add(createSkill("Inteligencia Emocional", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "es"));
            skills.add(createSkill("Emotional Intelligence", SOFT_SKILLS, 95, "https://api.iconify.design/flat-color-icons/idea.svg", "en"));
            
            skillRepo.saveAll(skills);

            // 4. Poblar Idiomas
            logger.info("Limpiando tabla Language...");
            languageRepo.deleteAll();
            List<LanguageEntity> languages = new ArrayList<>();

            // ES Content
            languages.add(createLanguage("Español", "Nativo", "es", 100, "es"));
            languages.add(createLanguage("Inglés", "Intermedio (B1/B2)", "en", 60, "es"));
            languages.add(createLanguage("Portugués", "Básico (A1/A2)", "pt", 20, "es"));

            // EN Content
            languages.add(createLanguage("Spanish", "Native", "es", 100, "en"));
            languages.add(createLanguage("English", "Intermediate (B1/B2)", "en", 60, "en"));
            languages.add(createLanguage("Portuguese", "Basic (A1/A2)", "pt", 20, "en"));
            
            languageRepo.saveAll(languages);
            
            // 5. Poblar Información del Proyecto (Nuevo)
            logger.info("Limpiando tabla ProjectInfo...");
            projectInfoRepo.deleteAll();
            List<ProjectInfoEntity> projectInfos = new ArrayList<>();

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

            projectInfos.add(createProjectInfo("Tecnologías", esTech, "es"));
            projectInfos.add(createProjectInfo("Technologies", enTech, "en"));
            projectInfos.add(createProjectInfo("Características Destacadas", esFeat, "es"));
            projectInfos.add(createProjectInfo("Key Features", enFeat, "en"));

            projectInfoRepo.saveAll(projectInfos);

            logger.info("✅ DataSeeder completado exitosamente.");

        } catch (Throwable e) {
            logger.error("❌ ERROR CRÍTICO en DataSeeder: {}", e.getMessage(), e);
            // No re-lanzamos la excepción para evitar detener la aplicación si ya está corriendo
        }
    }

    private void seedProfile(ProfileRepository repo, String lang) {
        if ("en".equals(lang)) {
            ProfileEntity profile = new ProfileEntity();
            profile.setId("1"); // Fixed ID for single profile
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
            profile.setId("1"); // ID fijo para perfil único
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

    private ExperienceEntity createExperience(String position, String company, String period, String description, String url, String lang) {
        ExperienceEntity exp = new ExperienceEntity();
        exp.setId(UUID.randomUUID().toString());
        exp.setTitle(position);
        exp.setCompany(company);
        exp.setPeriod(period);
        exp.setDescription(description);
        exp.setLink(url);
        exp.setLanguage(lang);
        return exp;
    }

    private SkillEntity createSkill(String name, String category, int level, String iconUrl, String lang) {
        SkillEntity skill = new SkillEntity();
        skill.setId(UUID.randomUUID().toString());
        skill.setName(name);
        skill.setCategory(category);
        skill.setLevel(level);
        skill.setIcon(iconUrl);
        skill.setLanguage(lang);
        return skill;
    }

    private EducationEntity createEducation(String degree, String institution, String period, String description, String url, String lang) {
        EducationEntity edu = new EducationEntity();
        edu.setId(UUID.randomUUID().toString());
        edu.setDegree(degree);
        edu.setInstitution(institution);
        edu.setPeriod(period);
        edu.setDescription(description);
        edu.setLink(url);
        edu.setLanguage(lang);
        return edu;
    }
    
    private LanguageEntity createLanguage(String name, String level, String code, int percentage, String lang) {
        LanguageEntity l = new LanguageEntity();
        l.setId(UUID.randomUUID().toString());
        l.setName(name);
        l.setLevel(level);
        l.setCode(code);
        l.setPercentage(percentage);
        l.setLanguage(lang);
        return l;
    }

    private ProjectInfoEntity createProjectInfo(String title, String content, String lang) {
        ProjectInfoEntity p = new ProjectInfoEntity();
        p.setId(UUID.randomUUID().toString());
        p.setCategory(title);
        p.setContent(content);
        p.setLanguage(lang);
        return p;
    }
}
