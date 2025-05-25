plugins {
    alias(libs.plugins.android.application)
    id("com.diffplug.spotless") version "6.25.0"
}
spotless {
    java {
        googleJavaFormat()
        target("src/**/*.java")
    }
}
android {
    namespace = "com.example.roomie"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.roomie"
        minSdk = 24
        targetSdk = 35
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
    //implementation("com.google.android.material:material:1.9.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.google.android.material:material:1.9.0")

    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // FlexboxLayout for tag‐wrapping
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")

}
