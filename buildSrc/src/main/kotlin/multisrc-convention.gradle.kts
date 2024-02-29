plugins {
    kotlin("android")
    id("com.android.library")
    id("org.jmailen.kotlinter")
}

android {
    namespace = "eu.kanade.tachiyomi.multisrc.${project.name}"

    compileSdk = AndroidConfig.COMPILE_SDK

    defaultConfig.minSdk = AndroidConfig.MIN_SDK

    sourceSets.named("main") {
        manifest.srcFile("AndroidManifest.xml")
        java.setSrcDirs(listOf("src"))
        res.setSrcDirs(listOf("res"))
        assets.setSrcDirs(listOf("assets"))
    }

    kotlinOptions {
        freeCompilerArgs += "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
    }
}

kotlinter {
    experimentalRules = true
    disabledRules = arrayOf(
            "experimental:argument-list-wrapping",
            "experimental:comment-wrapping",
    )
}

dependencies {
    val libs = versionCatalogs.named("libs")
    compileOnly(libs.findBundle("common").get())
}

tasks {
    preBuild { dependsOn(lintKotlin) }

    if (System.getenv("CI") != "true") {
        lintKotlin { dependsOn(formatKotlin) }
    }
}
