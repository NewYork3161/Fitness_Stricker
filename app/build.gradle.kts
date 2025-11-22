import java.util.Properties

plugins {
    // Use aliases from the version catalog
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // Add Kotlin Serialization plugin
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

// ⬇️ START: API KEY LOADING LOGIC
val properties = Properties().apply {
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}
// ⬆️ END: API KEY LOADING LOGIC

android {
    namespace = "com.example.fitness_striker"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.fitness_striker"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val apiKey = properties.getProperty("GEMINI_API_KEY") ?: ""
        buildConfigField("String", "GEMINI_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    // AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Gemini and Coroutines
    implementation(libs.google.ai.client.generativeai)
    implementation(libs.kotlinx.coroutines.android)

    // Kotlin Serialization runtime library
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
