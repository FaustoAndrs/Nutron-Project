git# Nutron

![Logotipo del proyecto](assets/logo_nutrox.png)

Aplicación multiplataforma Android/iOS para realizar un registro del conteo de calorias diarias. 

## 🚀 Características principales

* Registro de macronutrientes: Se pueden almacenar manualmente los valores de calorias, grasas, proteinas... proporcional a la cantidad consumida por el usuario.

* Lectura de codigo de barras: Se puede leer el codigo de barras de los productos para añadir con rapidez los macronutrientes al diario del usuario. 

* Los registros se pueden agrupar en: Nº Comidas y a su vez en recetas, platos, alimentos o productos que se podran consultar y reutilizar en futuos registros.

* Defnir tipo de dieta a seguir, personalizada mediante calculo basal, actividad fisica y el objetivo a conseguir por el usuario, deficit calorico, superávit calórico, recomposicion corporal, etc.

* Se mostrán graficos al usuario que describan el seguimiento de sus macronutrientes en relación a los objetivos definidos.
    
* Multiplataforma: Disponibilidad en iOS 26.3.1 y Android 16.
* Funcionalidad para sincronizar y respaldar de los datos generados por el usuario en la aplicación.

* (Ampliación):
  ** Login con Google y/o Apple.
  **Sugerencias de optimización de la dieta del usuario mediante IA 

## 🛠️ Tecnologías utilizadas

    *[KMP - Kotlin Multiplatform, Room, Koin, ...]
    *[Servidor Apache en Docker/kubernetes]
    *[Servidor MongoDb en Docker/kubernetes]
    * [API REST - Java]
    *[Libreria: openfoodfacts-kotlin]

## 🏗️ Arquitectura del Proyecto

Este proyecto sigue una arquitectura de servicios desacoplados para asegurar la escalabilidad:

    * Frontend (Kotlin Multiplatform): Lógica de negocio compartida entre Android e iOS.

    * Backend (API REST): Servidor web ejecutándose en un contenedor Docker.

    * Base de datos: MongoDB para el almacenamiento persistente.

    * Orquestación: Despliegue gestionado mediante Kubernetes para asegurar la disponibilidad.
    
    [Imagen](Arquitectura.jpg)

## 🏗️ Cómo ejecutar el proyecto

    Clona este repositorio: git clone [url-de-tu-repo]

    Instala las dependencias: [comando, ej: npm install]

    Ejecuta la aplicación: [comando de ejecución]

## Cómo ejecutar Docker

Consulta el archivo DOCKERCOMPOSE-README para más detalles.    

## 📜 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo LICENSE para más detalles.
