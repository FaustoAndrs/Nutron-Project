import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.compose)
            implementation(libs.androidx.security.crypto)
            implementation(libs.androidx.appcompat)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Iconos básicos
            implementation(libs.compose.icons.core)
            // Iconos extendidos
            implementation(libs.compose.icons.extended)

            // Navigation 3
            implementation(libs.jetbrains.navigation3.ui)
            implementation(libs.jetbrains.material3.adaptiveNavigation3)
            implementation(libs.kotlinx.serialization.json)

            // ViewModel
            implementation(libs.jetbrains.lifecycle.viewmodelNavigation3)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            // BarcodeScanning
            implementation(libs.kscan)

            // Permissions
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.compose)

            //Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)

            // Multiplatform Settings
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.multiplatform.settings.serialization)

            // Datetime
            implementation(libs.kotlinx.datetime)

            //Charts
            implementation(libs.charty)

        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.moko.permissions)
            implementation(libs.moko.permissions.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

android {
    namespace = "com.lazysyntax.nutron"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.lazysyntax.nutron"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.core.ktx)
    debugImplementation(libs.compose.uiTooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}
