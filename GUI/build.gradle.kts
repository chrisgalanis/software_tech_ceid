// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
buildscript {
    repositories {
        google()      // ← needed so AGP and Play-Services can be found
        mavenCentral()
    }
    dependencies {
        // make sure this matches the version of the Android application plugin you're using
        classpath("com.android.tools.build:gradle:8.9.1")
        // if you ever use Google-services plugin:
        // classpath("com.google.gms:google-services:4.3.15")

    }
}
