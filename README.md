🎬 Elevideo API - Plataforma VideoBoost
🚀 Índice

✨ Título e Imagen de Portada

🏅 Badges

💾 Descripción del Proyecto

🟢 Estado del Proyecto

💡 Características Implementadas

🔐 Sistema de Autenticación

☁️ Gestión de Videos y Multimedia

📧 Sistema de Emails

📚 Documentación de la API

🛠️ Acceso y Ejecución del Proyecto

☁️ Deploy en la Nube

💻 Tecnologías Utilizadas

👥 Personas Contribuyentes

👨‍💻 Desarrollador del Proyecto

📄 Licencia

📚 Referencias y Agradecimientos

✨ Elevideo API
Plataforma Backend para VideoBoost

Elevideo es una API RESTful moderna diseñada para gestionar usuarios, autenticación segura y almacenamiento de contenido multimedia para la plataforma VideoBoost.

El sistema permite:

Registro y autenticación segura de usuarios

Gestión de cuentas

Cambio seguro de contraseña

Manejo de videos y archivos multimedia

Integración con servicios cloud

Documentación automática de la API

La arquitectura está basada en Spring Boot siguiendo buenas prácticas de arquitectura limpia y seguridad moderna.

🏅 Badges

Estado: En Desarrollo Activo
Spring Boot 3
Java 17
PostgreSQL
JWT Authentication
Licencia MIT

💾 Descripción del Proyecto

Elevideo es el backend de la plataforma VideoBoost, un sistema pensado para gestionar contenido audiovisual y usuarios en un entorno moderno y escalable.

La API permite:

autenticación mediante JWT

gestión segura de usuarios

integración con Cloudinary para almacenamiento multimedia

envío de correos mediante Resend

persistencia de datos con PostgreSQL

documentación interactiva mediante Swagger

El proyecto está diseñado para ser desplegado en la nube utilizando Render.

🟢 Estado del Proyecto

🚧 Proyecto en Desarrollo Activo

Actualmente el sistema cuenta con:

Autenticación completa

Gestión de usuarios

Cambio de contraseña

Integración con servicios externos

Deploy funcional en la nube

Se planean futuras mejoras como:

gestión completa de videos

panel administrativo

sistema de roles y permisos

analytics de contenido

💡 Características Implementadas
👤 Gestión de Usuarios
Registro de usuario
POST /api/v1/auth/register


Permite registrar nuevos usuarios en la plataforma.

Las contraseñas se almacenan utilizando BCrypt hashing.

Inicio de sesión
POST /api/v1/auth/login


Autentica a un usuario y devuelve un token JWT.

El token se debe enviar en el header:

Authorization: Bearer {token}

Cambio de contraseña
PATCH /api/users/{id}/password


Permite cambiar la contraseña de un usuario autenticado.

Requiere:

contraseña actual

nueva contraseña

🔐 Sistema de Autenticación

La API utiliza JWT (JSON Web Tokens) para autenticación sin estado.

Flujo de autenticación:

1️⃣ El usuario inicia sesión
2️⃣ El servidor genera un JWT
3️⃣ El cliente envía el token en cada request protegido

Ejemplo de header:

Authorization: Bearer eyJhbGciOiJIUzM4NCJ9...


Ventajas:

escalabilidad

seguridad

autenticación stateless

☁️ Gestión de Multimedia

Elevideo integra Cloudinary para almacenamiento y gestión de archivos multimedia.

Funciones:

almacenamiento de videos

almacenamiento de imágenes

optimización automática

distribución mediante CDN

📧 Sistema de Emails

La plataforma utiliza Resend para el envío de correos transaccionales.

Casos de uso:

verificación de email

recuperación de contraseña

notificaciones del sistema

📚 Documentación de la API

La API incluye documentación interactiva con Swagger.

Permite:

explorar endpoints

probar requests

visualizar modelos de datos

autenticarse directamente desde la UI

Acceso:

http://localhost:8081/swagger-ui.html


o en producción:

https://s02-26-e16-wad.onrender.com/swagger-ui.html

🛠️ Acceso y Ejecución del Proyecto
🔧 Requisitos Previos

Java 17+

Maven 3+

PostgreSQL

IDE (IntelliJ recomendado)

Clonar el repositorio
git clone https://github.com/No-Country-simulation/S02-26-E16-WAD
cd elevideo-backend

Configurar base de datos

Crear base de datos PostgreSQL:

CREATE DATABASE elevideo_db;


Configurar en application.yml o variables de entorno.

Ejecutar la aplicación

Primero instalar dependencias:

mvn clean install


Luego ejecutar:

mvn spring-boot:run


La API estará disponible en:

http://localhost:8081

☁️ Deploy en la Nube

La aplicación está desplegada en Render.

Infraestructura utilizada:

Web Service (Spring Boot API)

PostgreSQL Database

URL de producción:

https://s02-26-e16-wad.onrender.com

💻 Tecnologías Utilizadas
Backend

Java 17

Spring Boot 3

Spring Security

Spring Data JPA

Hibernate

Seguridad

JWT Authentication

BCrypt Password Encoding

Base de Datos

PostgreSQL

Servicios Cloud

Cloudinary → almacenamiento multimedia

Resend → envío de correos

Documentación

SpringDoc OpenAPI

Swagger UI

Deploy

Render

Herramientas

Maven

Lombok

Postman

Swagger

👥 Personas Contribuyentes

Actualmente el proyecto está abierto a contribuciones.

Si deseas colaborar:

crear nuevas funcionalidades

mejorar documentación

optimizar seguridad

añadir testing

¡Las contribuciones son bienvenidas!

👨‍💻 Desarrolladores del Proyecto

Lisandro
Jhorman
David
Edgar
Eduin




GitHub:

https://github.com/No-Country-simulation/S02-26-E16-WAD


Proyecto desarrollado como parte de prácticas avanzadas en:

Arquitectura de APIs

Seguridad con Spring

Integración con servicios cloud

Deploy en la nube

📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT.

Puedes usarlo, modificarlo y distribuirlo libremente.

Consulta el archivo:

LICENSE


para más información.

📚 Referencias y Agradecimientos

Formación en backend proporcionada por Alura Latam

Inspiración en buenas prácticas de arquitectura del proyecto Voll.med

Documentación generada con SpringDoc OpenAPI

Integraciones con Cloudinary y Resend

🚀 Elevideo — VideoBoost Backend

Plataforma backend moderna para la gestión de contenido audiovisual, usuarios y autenticación segura en la nube.
