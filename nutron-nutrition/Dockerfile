# --- Etapa 1: Compilación (Build) ---
FROM eclipse-temurin:21-jdk-jammy AS build

# Definir el directorio de trabajo
WORKDIR /app

# Copiar los archivos de configuración de Gradle primero (para aprovechar el caché de capas)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
# Nota: Si usas Groovy en lugar de Kotlin, cambia la extensión a .gradle

# Dar permisos de ejecución al wrapper de Gradle
RUN chmod +x ./gradlew

# Descargar las dependencias (esto fallará si no hay código, pero ayuda al caché)
RUN ./gradlew dependencies --no-daemon

# Copiar el código fuente
COPY src src

# Compilar y generar el JAR (omitiendo tests para acelerar el proceso de laboratorio)
RUN ./gradlew bootJar -x test --no-daemon

# --- Etapa 2: Ejecución (Runtime) ---
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copiar solo el JAR generado desde la etapa de compilación
COPY --from=build /app/build/libs/*-SNAPSHOT.jar app.jar

# Exponer el puerto que configuramos en application.properties
EXPOSE 8081

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]