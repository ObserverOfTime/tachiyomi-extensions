plugins {
    id("com.android.library")
}

android {
    namespace = "eu.kanade.tachiyomi.extension.core"

    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig.minSdk = AndroidConfig.MIN_SDK

    sourceSets.named("main") {
        manifest.srcFile("AndroidManifest.xml")
        res.setSrcDirs(listOf("res"))
    }

    buildFeatures {
        resValues = false
        shaders = false
    }
}
