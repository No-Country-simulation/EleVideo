# Elevideo — Backend

API REST para la plataforma **Elevideo**, un sistema de procesamiento de video que permite subir videos horizontales y convertirlos automáticamente al formato vertical 9:16 con detección de rostros, reencuadre inteligente y múltiples modos de procesamiento.

---

## Tabla de contenidos

- [Descripción general](#descripción-general)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Módulos](#módulos)
- [Endpoints](#endpoints)
- [Autenticación](#autenticación)
- [Variables de entorno](#variables-de-entorno)
- [Ejecución local](#ejecución-local)
- [Documentación API](#documentación-api)

---

## Descripción general

El backend de Elevideo es un **monolito modular** construido con Spring Boot 3 y Spring Modulith. Se encarga de:

- Gestión de usuarios y autenticación con JWT
- Organización de videos en proyectos
- Subida de videos a Cloudinary
- Delegación del procesamiento de video a un microservicio Python vía HTTP
- Recepción de resultados a través de webhooks
- Notificaciones por email con Thymeleaf + Resend

El microservicio Python realiza el procesamiento real: reencuadre 9:16, detección de rostros con MediaPipe, estabilización de cámara y generación de clips cortos.

---

## Arquitectura

```
com.elevideo.backend
├── shared/          ← Infraestructura técnica transversal
├── auth/            ← Registro, login, verificación de email
├── user/            ← Perfil de usuario autenticado
├── project/         ← CRUD de proyectos
├── video/           ← Subida y gestión de videos
├── processing/      ← Jobs de procesamiento y renditions
└── notification/    ← Envío de emails por eventos de dominio
```

### Reglas de dependencia entre módulos

Cada módulo expone únicamente su paquete `api/` al resto. Ningún módulo importa el paquete `internal/` de otro.

```
shared        ← todos los módulos dependen de él
notification  ← solo escucha eventos, nunca es llamado directamente
auth          ──► user.api
user          ──► shared
project       ──► shared
video         ──► project.api
processing    ──► video.api
notification  ◄── eventos de auth, user y processing
```

---

## Tecnologías

| Categoría | Tecnología | Versión |
|---|---|---|
| Framework | Spring Boot | 3.5.11 |
| Arquitectura modular | Spring Modulith | 2.0.3 |
| Lenguaje | Java | 17 |
| Base de datos | PostgreSQL | — |
| ORM | Spring Data JPA / Hibernate | — |
| Seguridad | Spring Security + JWT (JJWT) | 0.12.5 |
| Almacenamiento de video | Cloudinary | 1.39.0 |
| Mapeo de objetos | MapStruct | 1.6.3 |
| Reducción de boilerplate | Lombok | — |
| Documentación API | SpringDoc OpenAPI (Swagger) | 2.8.15 |
| Plantillas de email | Thymeleaf | — |
| Validación | Jakarta Bean Validation | — |
| Monitoreo | Spring Boot Actuator | — |

---

## Módulos

### `shared`
Infraestructura técnica compartida: configuración de seguridad, filtros JWT, proveedor del usuario autenticado (`CurrentUserProvider`), publicador de eventos de dominio, manejo global de excepciones (`GlobalExceptionHandler`) y respuesta paginada genérica (`PageResponse<T>`).

### `auth`
Gestiona el ciclo de vida de autenticación:
- Registro con verificación de email
- Login con emisión de JWT
- Recuperación y reseteo de contraseña

### `user`
Operaciones sobre el perfil del usuario autenticado: consulta, actualización de datos, cambio de contraseña y eliminación de cuenta.

### `project`
CRUD de proyectos. Cada proyecto pertenece a un usuario y agrupa sus videos. La respuesta incluye `videoCount` con el número actual de videos del proyecto.

### `video`
Subida de videos a Cloudinary y gestión del catálogo. Los videos se almacenan con metadatos (duración, dimensiones, formato, tamaño). Soporta búsqueda por título y filtrado por estado.

### `processing`
Núcleo del negocio. Envía videos al microservicio Python para su procesamiento y gestiona el ciclo de vida de los jobs:
- Creación de jobs con tres modos: `vertical`, `short_auto`, `short_manual`
- Consulta de estado en tiempo real (polling)
- Cancelación de jobs activos
- Vista general con jobs activos y finalizados
- Gestión de renditions (resultados procesados) con sus URLs de descarga

### `notification`
Escucha eventos de dominio y envía emails transaccionales. No expone ningún servicio público — actúa únicamente como consumidor de eventos internos.

---

## Endpoints

### Auth — `/api/v1/auth`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/register` | Registro de nuevo usuario | Pública |
| POST | `/login` | Inicio de sesión | Pública |
| POST | `/verify-email` | Verificación de email con token | Pública |
| POST | `/forgot-password` | Solicitar reseteo de contraseña | Pública |
| POST | `/reset-password` | Confirmar nueva contraseña | Pública |

### Users — `/api/v1/users`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| GET | `/me` | Obtener perfil del usuario autenticado | JWT |
| PATCH | `/me` | Actualizar nombre o descripción | JWT |
| PATCH | `/me/password` | Cambiar contraseña | JWT |
| DELETE | `/me` | Eliminar cuenta | JWT |

### Projects — `/api/v1/projects`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/` | Crear proyecto | JWT |
| GET | `/` | Listar proyectos del usuario | JWT |
| GET | `/{projectId}` | Obtener proyecto por ID | JWT |
| PUT | `/{projectId}` | Actualizar proyecto | JWT |
| DELETE | `/{projectId}` | Eliminar proyecto | JWT |

### Videos — `/api/v1/projects/{projectId}/videos`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/` | Subir video (multipart) | JWT |
| GET | `/` | Listar videos del proyecto | JWT |
| GET | `/{videoId}` | Obtener video por ID | JWT |
| DELETE | `/{videoId}` | Eliminar video | JWT |

### Processing — `/api/v1/projects/{projectId}/videos/{videoId}`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/jobs` | Crear job de procesamiento | JWT |
| GET | `/jobs/overview` | Vista general de jobs activos y finalizados | JWT |
| GET | `/jobs/{jobId}` | Estado actual de un job (polling) | JWT |
| POST | `/jobs/{jobId}/cancel` | Cancelar un job activo | JWT |
| GET | `/renditions` | Listar resultados procesados | JWT |
| GET | `/renditions/{renditionId}` | Obtener rendition por ID | JWT |
| DELETE | `/renditions/{renditionId}` | Eliminar rendition | JWT |

### Webhooks internos — `/api/internal/webhooks`

| Método | Ruta | Descripción | Auth |
|---|---|---|---|
| POST | `/job-completed` | Notificación de job completado desde Python | `X-Service-Key` |
| POST | `/job-progress` | Actualización de progreso desde Python | `X-Service-Key` |

---

## Autenticación

El sistema usa dos mecanismos de autenticación:

**JWT (usuarios):** Los endpoints de negocio requieren el header:
```
Authorization: Bearer <token>
```
El token se obtiene en `POST /api/v1/auth/login` y tiene expiración configurable. Los tokens invalidados se almacenan en una blacklist hasta su expiración.

**X-Service-Key (servicios internos):** Los webhooks del microservicio Python se autentican con:
```
X-Service-Key: <clave-secreta>
```
Solo los endpoints bajo `/api/internal/**` aceptan este esquema. El filtro `InternalServiceAuthFilter` otorga el rol `ROLE_INTERNAL_SERVICE`.

---

## Variables de entorno

```properties
# =========================
# DATABASE
# =========================
DB_HOST=localhost
DB_PORT=5432
DB_NAME=your_database_name
DB_USER=your_database_user
DB_PASSWORD=your_database_password

# =========================
# JWT
# =========================
JWT_SECRET=your_base64_encoded_secret
JWT_EXPIRATION=3600000
JWT_EMAIL_EXPIRATION=1800000
JWT_PYTHON_EXPIRATION=300000

# =========================
# RESEND (EMAIL SERVICE)
# =========================
RESEND_APIKEY=your_resend_api_key
RESEND_EMAIL=your_verified_sender_email
RESEND_NAME=YourAppName

# =========================
# CLOUDINARY
# =========================
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_cloudinary_api_key
CLOUDINARY_API_SECRET=your_cloudinary_api_secret

# =========================
# CORS
# =========================
CORS_ALLOWED=http://localhost:*

# ============================================================
# Configuración del microservicio Python de procesamiento de video
# ============================================================
PYTHON_SERVICE_BASE_URL=your_url_base_python
PYTHON_SERVICE_APIKEY=your_api_key
```

---

## Ejecución local

### Requisitos previos

- Java 17+
- Maven 3.8+
- PostgreSQL 14+
- Microservicio Python de Elevideo en ejecución (para el procesamiento de video)

### Pasos

```bash
# 1. Clonar el repositorio
git clone https://github.com/tu-usuario/elevideo-backend.git
cd elevideo-backend

# 2. Configurar variables de entorno (o editar application.properties)
cp .env.example .env

# 3. Crear la base de datos
createdb elevideo

# 4. Compilar y ejecutar
mvn spring-boot:run
```

La aplicación arranca en `http://localhost:8081` o el puerto especificado por el usuario. 

---

## Documentación API

Con la aplicación en ejecución, la documentación interactiva de Swagger UI está disponible en:

```
http://localhost:8081/swagger-ui/index.html
```

La especificación OpenAPI en formato JSON:

```
http://localhost:8081/v3/api-docs
```

Todos los endpoints están documentados con ejemplos de request y response, incluyendo los distintos modos de procesamiento (`vertical`, `short_auto`, `short_manual`) y los posibles errores por endpoint.