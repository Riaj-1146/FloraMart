plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    // add for firebase
    ("classpath 'com.google.gms:goolge-services'")
}

android {
    namespace = "com.example.flora_mart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.flora_mart"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // add for firebase
    ("implementation platfrom('com.google.firebase:firebase-bom:33.7.0')")
    ("implementation 'com.google.firebase:firebase-auth:23.1.0'")
}