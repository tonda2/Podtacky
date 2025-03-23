import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("local.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "cz.tonda2.podtacky"
    compileSdk = 34

    defaultConfig {
        applicationId = "cz.tonda2.podtacky"
        minSdk = 24
        targetSdk = 34
        versionCode = 26
        versionName = "1.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = keystoreProperties["DEBUG_KEY_ALIAS"].toString()
            keyPassword = keystoreProperties["DEBUG_KEY_PASSWORD"].toString()
            storeFile = file(keystoreProperties["DEBUG_STORE_FILE"].toString())
            storePassword = keystoreProperties["DEBUG_STORE_PASSWORD"].toString()
        }
        create("release") {
            keyAlias = keystoreProperties["RELEASE_KEY_ALIAS"].toString()
            keyPassword = keystoreProperties["RELEASE_KEY_PASSWORD"].toString()
            storeFile = file(keystoreProperties["RELEASE_STORE_FILE"].toString())
            storePassword = keystoreProperties["RELEASE_STORE_PASSWORD"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation(libs.androidx.work.runtime.ktx)
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation("io.coil-kt:coil-compose:2.6.0")

    val okHttpVersion = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$okHttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okHttpVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    implementation(platform("io.insert-koin:koin-bom:3.5.3"))
    implementation("io.insert-koin:koin-android")
    implementation("io.insert-koin:koin-androidx-compose")

    implementation("com.google.accompanist:accompanist-pager:0.34.0")
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation(libs.zoomable)
    implementation(libs.core)
}