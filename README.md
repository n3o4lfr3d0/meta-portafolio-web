# 🚀 Portfolio Profesional - Alfredo Soto

Este repositorio contiene el código fuente de mi portafolio profesional personal. Es una aplicación web moderna Fullstack diseñada para mostrar experiencia, habilidades y proyectos, integrando características dinámicas gestionadas por un panel de administración y potenciadas por Inteligencia Artificial.

## 🏗 Arquitectura y Tecnologías

El proyecto sigue una arquitectura de **Cliente-Servidor (REST API)**, desacoplada y desplegada en la nube.

### 🎨 Frontend (Cliente)
Desarrollado con **Angular 21+**, enfocado en rendimiento, accesibilidad y una experiencia de usuario fluida.
*   **Framework:** Angular 21 (Standalone Components, Signals).
*   **Estilos:** Tailwind CSS + DaisyUI.
*   **Interactividad:** Driver.js (Tour guiado), GSAP (Animaciones).
*   **Testing:** Vitest, Cypress (E2E).
*   **Hosting:** Vercel.

### ⚙️ Backend (Servidor)
Construido con **Java 21** y **Spring Boot 3.4**, proporcionando una API robusta y segura.
*   **Framework:** Spring Boot 3.4.1.
*   **Seguridad:** Spring Security + JWT (Authentication & Authorization).
*   **Base de Datos:** AWS DynamoDB (NoSQL Serverless).
*   **AI Integration:** Google Gemini Pro (para el Chatbot asistente).
*   **Documentación API:** Swagger/OpenAPI (si aplica).
*   **Hosting:** Railway.

### ☁️ Infraestructura
*   **Base de Datos:** AWS DynamoDB (Región: us-east-1).
*   **CI/CD:** Despliegues automáticos configurados en Vercel (Frontend) y Railway (Backend) vía GitHub.

---

## ✨ Características Principales

*   **Gestión de Contenido Dinámico (CMS):** Las secciones de Experiencia, Educación, Habilidades y Proyectos son editables desde un panel de administración seguro, sin necesidad de redesplegar.
*   **Asistente Virtual con IA:** Un chatbot integrado (impulsado por Gemini Pro) que responde preguntas sobre mi perfil profesional basándose en el contexto del portafolio.
*   **Internacionalización (i18n):** Soporte completo para Español e Inglés.
*   **Modo Oscuro/Claro:** Detección automática y toggle manual.
*   **Generación de CV:** Enlaces directos para descargar el CV actualizado en PDF.
*   **Tour Interactivo:** Guía de bienvenida para nuevos visitantes usando Driver.js.
*   **Soft Delete:** Implementación de borrado lógico en base de datos para seguridad de la información.

---

## 🚀 Instalación y Puesta en Marcha

### Prerrequisitos
*   Java 21 JDK
*   Node.js 20+ (LTS recomendado)
*   Maven 3.9+
*   Cuenta de AWS (para DynamoDB) y Google AI Studio (para Gemini API)

### 1. Clonar el Repositorio
```bash
git clone https://github.com/n3o4lfr3d0/meta-portafolio-web.git
cd meta-portafolio-web
```

### 2. Configuración del Backend
Navega al directorio `backend`:
```bash
cd backend
```

Crea un archivo de variables de entorno o configúralas en tu IDE/Sistema:
*   `AWS_ACCESS_KEY_ID`: Tu Key ID de AWS.
*   `AWS_SECRET_ACCESS_KEY`: Tu Secret Key de AWS.
*   `AWS_REGION`: us-east-1 (o tu región preferida).
*   `GEMINI_API_KEY_PERSONAL`: Tu API Key de Google Gemini.
*   `JWT_SECRET`: Una cadena larga y segura para firmar tokens.
*   `ADMIN_PASSWORD`: Contraseña para el usuario administrador inicial.

Ejecuta la aplicación:
```bash
mvn spring-boot:run
```
El servidor iniciará en `http://localhost:8080`.

### 3. Configuración del Frontend
Navega al directorio `frontend`:
```bash
cd frontend
```

Instala las dependencias:
```bash
npm install
```

Inicia el servidor de desarrollo:
```bash
npm start
```
La aplicación estará disponible en `http://localhost:4200`.

---

## 🧪 Testing

### Backend
Para ejecutar las pruebas unitarias y de integración (JUnit 5 + Mockito):
```bash
cd backend
mvn test
```

### Frontend
Para ejecutar las pruebas unitarias (Vitest):
```bash
cd frontend
npm test
```
Para pruebas E2E (Cypress):
```bash
npm run cypress:open
```

---

## 🤝 Contribución y Contacto
Este es un proyecto personal, pero el feedback es bienvenido. Si encuentras un bug o tienes una sugerencia, por favor abre un issue.

**Desarrollado por Alfredo Soto**
*   [LinkedIn](https://linkedin.com/in/alfredosotonolazco)
*   [Email](mailto:alfredosotonolazco@gmail.com)
