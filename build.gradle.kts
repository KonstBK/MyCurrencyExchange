// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false
    id("androidx.navigation.safeargs") version "2.4.2" apply false


}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val navVersion = "2.7.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
    }

}
