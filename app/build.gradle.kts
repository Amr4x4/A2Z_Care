plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.a2zcare"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.a2zcare"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Add this for Room schema export (optional but recommended for debugging)
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose BOM and UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Material Design
    implementation(libs.material)

    // Splash API
    implementation(libs.androidx.core.splashscreen)

    // Fonts
    implementation(libs.androidx.ui.text.google.fonts)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // Pager and Indicators - Accompanist
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)

    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit for networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Coroutines for asynchronous calls
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // Google Sign-In
    implementation(libs.play.services.auth)

    // System UI Controller
    implementation(libs.accompanist.systemuicontroller)

    // Compose Material Icons
    implementation(libs.androidx.material.icons.extended)

    // Location Services
    implementation(libs.play.services.location)

    // Coil for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.gif) // for GIF support

    // Lifecycle ViewModel & LiveData
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation (libs.accompanist.permissions)

    implementation (libs.accompanist.pager.v0280)
    implementation(libs.logging.interceptor)

    implementation(libs.ycharts)
    implementation(libs.mpandroidchart)

}