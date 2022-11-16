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
    }
}
