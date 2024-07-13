plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.view.wordwise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.view.wordwise"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =  "com.view.wordwise.CustomTestRunner"
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


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation (libs.androidx.lifecycle.lifecycle.viewmodel.compose3)

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.51.1")
    implementation(libs.androidx.runner)
    implementation(libs.core)
    implementation(libs.androidx.junit.ktx)
    kapt ("com.google.dagger:hilt-compiler:2.51.1")
    implementation (libs.androidx.runtime.livedata)

    //Retrofit
    implementation (libs.squareup.retrofit)
    implementation (libs.squareup.converter.gson)
    //Room
    implementation(libs.room.runtime.v260)
    ksp (libs.androidx.room.compiler.v260)
    implementation (libs.androidx.room.ktx)

    // Testing dependencies
    testImplementation (libs.junit)
    testImplementation (libs.mockito.core)
    testImplementation (libs.mockito.kotlin)
    testImplementation (libs.androidx.core.testing)
    testImplementation (libs.kotlinx.coroutines.test)
    // Hilt testing
    testImplementation(libs.com.google.dagger.hilt.android.testing2)
    kaptTest(libs.hilt.android.compiler)
    testAnnotationProcessor(libs.hilt.android.compiler)
    androidTestImplementation(libs.com.google.dagger.hilt.android.testing2)
    kaptAndroidTest(libs.hilt.android.compiler)
    androidTestAnnotationProcessor(libs.hilt.android.compiler)


    androidTestImplementation (libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

}
kapt {
    correctErrorTypes = true
}
