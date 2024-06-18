plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.dizzi.radio"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dizzi.radio"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")



    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("com.google.accompanist:accompanist-webview:0.28.0")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Gson
    implementation("com.google.code.gson:gson:2.11.0")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.9")
    //logging
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-ktx:1.9.0")

    //dagger-hilt
    implementation("com.google.dagger:hilt-android:2.51")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51")

    //coil
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")

    //paging
    implementation ("androidx.paging:paging-compose:3.3.0")
    implementation ("androidx.paging:paging-runtime-ktx:3.3.0")
    implementation("androidx.paging:paging-common-ktx:3.3.0")

    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation ("androidx.activity:activity-ktx:1.9.0")
    implementation ("androidx.fragment:fragment-ktx:1.8.0")

    implementation ("androidx.activity:activity-compose:1.9.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    implementation ("androidx.compose.runtime:runtime-livedata:1.6.8")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")

    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-database")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}