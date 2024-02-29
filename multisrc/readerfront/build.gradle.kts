plugins {
    id("multisrc-convention")
    id("kotlinx-serialization")
}

baseVersionCode = 9

dependencies {
    api(project(":lib:i18n"))
}
