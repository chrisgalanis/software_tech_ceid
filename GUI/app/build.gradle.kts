// app/build.gradle

plugins {
    alias(libs.plugins.android.application)
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
    // If you are using dataBinding or viewBinding, ensure they are enabled here
    // buildFeatures {
    //     viewBinding = true
    //     // dataBinding = true // if you use it
    // }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity) // Make sure you have defined 'activity' in your libs.versions.toml
                                  // e.g., androidx-activity = { group = "androidx.activity", name = "activity-ktx", version = "1.9.0" }
                                  // and then activity = { alias = "androidx-activity" }
                                  // Or if it's just 'androidx.activity:activity', ensure the alias is correct.
    implementation(libs.constraintlayout)

    // Add this line for CircleImageView:
    implementation(libs.circleimageview)

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
