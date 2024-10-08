plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.multitaskar"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.multitaskar"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.core:core-splashscreen:1.0.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-firestore:24.11.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")
    implementation ("com.firebaseui:firebase-ui-firestore:8.0.2")
    implementation ("com.airbnb.android:lottie:6.4.0")
    implementation ("com.google.android.gms:play-services-ads:23.0.0")
    implementation ("com.github.KwabenBerko:News-API-Java:1.0.2")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.karumi:dexter:6.2.3")
    implementation ("com.github.webzatec:AI_Webza_Tec:1.0")
    implementation ("com.wdullaer:materialdatetimepicker:4.2.3")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.7.0")
    implementation("com.google.guava:guava:31.0.1-android")
    implementation("org.reactivestreams:reactive-streams:1.0.4")
}