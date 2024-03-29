buildscript {
    repositories {
        google()
        mavenCentral()
        mavenLocal()
        mavenCentral { uri("https://jitpack.io") }
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.2.0")
        val kotlin_version by extra("1.7.10") // Use the latest stable version that's compatible
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.android.library") version "8.2.0" apply false
}

/*
id("com.android.application") version "7.2.0" apply false
id("org.jetbrains.kotlin.android") version "1.6.21" apply false
id("com.android.library") version "7.2.0" apply false*/
