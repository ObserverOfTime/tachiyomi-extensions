plugins {
    kotlin("android")
    id("com.android.library")
}

android {
    namespace = "eu.kanade.tachiyomi.lib.${project.name}"

    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig.minSdk = AndroidConfig.MIN_SDK

    buildFeatures {
        androidResources = false
        resValues = false
        shaders = false
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    val libs = versionCatalogs.named("libs")
    compileOnly(libs.findBundle("common").get())
}
