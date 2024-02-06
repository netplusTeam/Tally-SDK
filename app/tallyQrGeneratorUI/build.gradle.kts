plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("maven-publish")
}

group = "com.netplus.tallyqrgeneratorui"
version = "1.0.0"

android {
    namespace = "com.netplus.tallyqrgeneratorui"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    // If you are using Kotlin
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    //implementation("com.google.android.material:material:1.11.0")
    implementation(project(":app:coreMechanism"))
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:8.2.1")
    implementation("com.google.android.material:material:1.4.0") // Use the version appropriate for your project
    //implementation("com.android.databinding:compiler:3.5.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.databinding:databinding-runtime:8.2.1")
    implementation("org.osmdroid:osmdroid-android:6.1.10")
    implementation("org.apache.commons:commons-lang3:3.9")
    implementation("io.insert-koin:koin-android:3.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}