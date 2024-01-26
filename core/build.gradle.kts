plugins {
    id("com.android.library")
}

android {
    namespace = "eu.kanade.tachiyomi.extension.core"

    defaultConfig {
        minSdk = AndroidConfig.minSdk
        compileSdk = AndroidConfig.compileSdk
    }

    sourceSets {
        named("main") {
            manifest.srcFile("AndroidManifest.xml")
            res.setSrcDirs(listOf("res"))
        }
    }

    buildFeatures {
        resValues = false
        shaders = false
    }
}
