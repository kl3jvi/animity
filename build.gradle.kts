import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.version

plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    id("org.jetbrains.kotlin.android") version "1.8.0" apply false
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.com.google.dagger.hilt.android.gradle.plugin)
        classpath(libs.com.google.gms.google.services)
        classpath(libs.com.google.firebase.firebase.crashlytics.gradle)
        classpath(libs.com.google.android.libraries.mapsplatform.secrets.gradle.plugin)
        classpath(libs.gradle.plugin.com.onesignal.onesignal.gradle.plugin)
        classpath(libs.androidx.navigation.navigation.safe.args.gradle.plugin)
        classpath(libs.org.jetbrains.kotlin.kotlin.gradle.plugin)
        classpath(libs.perf.plugin)
    }
}
