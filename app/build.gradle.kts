plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "my.edu.utem.gymmembership"
    compileSdk = 35

    defaultConfig {
        applicationId = "my.edu.utem.gymmembership"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "AUTH_BASE_URL", "\"${project.findProperty("AUTH_BASE_URL_DEBUG")}\"")
            buildConfigField("String", "CRUD_BASE_URL", "\"${project.findProperty("CRUD_BASE_URL_DEBUG")}\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "AUTH_BASE_URL", "\"${project.findProperty("AUTH_BASE_URL_RELEASE")}\"")
            buildConfigField("String", "CRUD_BASE_URL", "\"${project.findProperty("CRUD_BASE_URL_RELEASE")}\"")
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
        viewBinding = true
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Jetpack Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Retrofit for Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // OkHttp for interceptors (e.g., JWT token)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Room for local database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    // (Optional) In-app Push Notifications e.g., Firebase Cloud Messaging
    implementation(libs.firebase.messaging.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.hilt.android.v254)
    ksp(libs.hilt.compiler.v254)
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.androidx.compose.material.v150alpha09)
    implementation(libs.kotlinx.datetime)

}