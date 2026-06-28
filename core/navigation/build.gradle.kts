plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "abika.sinau.moviedbnew.core.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
