plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.facebookclone"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.facebookclone"
        minSdk = 24
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

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    // firebase realtime
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-database")
    // storage
    implementation("com.google.firebase:firebase-storage")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("nl.joery.animatedbottombar:library:1.1.0")
    // circular imageview
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // round image view
    implementation ("com.makeramen:roundedimageview:2.3.0")
    // picasso android
    implementation ("com.squareup.picasso:picasso:2.71828")
    // time ago android studio
    implementation("com.github.marlonlom:timeago:4.0.3")
    // story view
    implementation ("com.github.OMARIHAMZA:StoryView:1.0.2-alpha")
    //Shimmer
    implementation ("com.github.sharish:ShimmerRecyclerView:v1.3")
    // thư Zego CALL UI KIT
    implementation ("com.github.ZEGOCLOUD:zego_uikit_prebuilt_call_android:+")
}