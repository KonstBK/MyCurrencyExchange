plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    namespace = "com.example.mycurrencyexchange"
    compileSdk = 33

    kapt {
        arguments {
            arg("key", "value")
        }
        correctErrorTypes = true
    }

    hilt {
        enableAggregatingTask = true
    }

    defaultConfig {
        applicationId = "com.example.mycurrencyexchange"
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

    kotlinOptions {
        jvmTarget = "1.8"
}

dependencies {
    val roomVersion = "2.4.3"
    val appCompatVersion = "1.4.0"
    val activityVersion = "1.4.0"


    implementation ("androidx.appcompat:appcompat:$appCompatVersion")
    implementation ("androidx.activity:activity-ktx:$activityVersion")

    // Room components
    implementation ("androidx.room:room-runtime:$roomVersion")
    kapt ("androidx.room:room-compiler:2.3.0")
    androidTestImplementation ("androidx.room:room-testing:$roomVersion")
    implementation ("androidx.room:room-ktx:$roomVersion")


    implementation ("androidx.appcompat:appcompat:1.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation ("androidx.navigation:navigation-ui-ktx:2.6.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi-adapters:+")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")

    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}}