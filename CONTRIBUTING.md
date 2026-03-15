# Guía de Contribución 🚀

¡Gracias por querer mejorar este proyecto! Para mantener la calidad del código y un historial de cambios limpio, seguimos estas normas:

## 🌿 Flujo de Trabajo de Ramas

Usamos el modelo de ramificación basado en **GitFlow simplificado**. Todas las ramas de tareas deben nacer desde `develop`.

### Estándar de Nombramiento
Las ramas deben usar el formato `categoría/descripción-breve` en minúsculas:

| Categoría | Descripción | Ejemplo |
| :--- | :--- | :--- |
| `feature/` | Nueva funcionalidad | `feature/login-biometrico` |
| `bugfix/` | Corrección de error en desarrollo | `bugfix/error-auth` |
| `hotfix/` | Error crítico en producción (nace de `main`) | `hotfix/caida-servidor` |
| `docs/` | Cambios en documentación | `docs/actualizar-api` |

---

## 💬 Convención de Commits

Este proyecto utiliza **Conventional Commits**. El formato es:  
`<tipo>(entorno opcional): <descripción en imperativo y minúsculas>`

### Tipos permitidos:
* `feat`: Una nueva característica.
* `fix`: Una corrección de un error.
* `docs`: Cambios en la documentación.
* `style`: Formateo, puntos y coma faltantes, etc. (sin cambios de lógica).
* `refactor`: Cambio de código que no corrige errores ni añade funciones.
* `chore`: Actualización de tareas de construcción, dependencias, etc.

> **Nota:** Si el mensaje no cumple con este formato, el commit será rechazado automáticamente por nuestro sistema de validación.

---

## 🛠️ Proceso de Desarrollo

1.  Asegúrate de estar en la rama de integración: `git checkout develop`.
2.  Sincroniza con el servidor: `git pull origin develop`.
3.  Crea tu rama de tarea: `git checkout -b feature/mi-nueva-tarea`.
4.  Realiza tus cambios y haz commits siguiendo el estándar.
5.  Al terminar, sube tu rama: `git push origin feature/mi-nueva-tarea`.
6.  Abre un **Pull Request** hacia la rama `develop`.

---

## 📦 Lanzamiento de Versiones (Releases)

Solo los administradores ejecutan el comando de release. Al ejecutar `npm run release`, el sistema:
1. Actualiza la versión en `package.json`.
2. Genera automáticamente el `CHANGELOG.md`.
3. Crea un tag de Git con la versión correspondiente.
