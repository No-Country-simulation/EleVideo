# EleVideo Frontend

Frontend del proyecto **EleVideo**, desarrollado en **React.js** con **JavaScript**, utilizando **CRACO** y **Tailwind CSS**.  
El proyecto está organizado de manera modular con componentes, hooks y contexto global para facilitar escalabilidad y mantenibilidad.

---

## Tecnologías principales

- **React.js** – Librería para construir interfaces de usuario reactivas.
- **JavaScript (ES6+)** – Lenguaje principal del proyecto.
- **CRACO** – Configuración de React sin eject.
- **Tailwind CSS** – Framework CSS utilitario para estilos rápidos y consistentes.
- **PostCSS** – Procesamiento y optimización de CSS.
- **Git** – Control de versiones.

---

## Instalación

Clona el repositorio:

```bash
git clone https://github.com/No-Country-simulation/EleVideo.git
cd Frontend
```

##Instala las dependencias:
```bash
npm install
```

##Scripts disponibles
Levantar servidor de desarrollo:
```bash
npm start
```

Construir la aplicación para producción:
```bash
npm run build
```

##Estructura de carpetas
```
Frontend/
│
├─ public/                 # Archivos estáticos (HTML, imágenes, favicon)
├─ src/
│  ├─ api/                 # Llamadas a servicios externos
│  ├─ components/          # Componentes reutilizables
│  ├─ context/             # Contextos de React para estado global
│  ├─ hooks/               # Custom hooks
│  ├─ lib/                 # Librerías internas o utilidades
│  ├─ pages/               # Páginas principales de la aplicación
│  ├─ App.js               # Componente principal
│  └─ index.js             # Punto de entrada de React
├─ tailwind.config.js      # Configuración de Tailwind
├─ postcss.config.js       # Configuración de PostCSS
├─ craco.config.js         # Configuración de CRACO
├─ package.json            # Dependencias y scripts
└─ package-lock.json       # Control de versiones de dependencias
```

## Licencia

Este proyecto se encuentra bajo la licencia **MIT**.