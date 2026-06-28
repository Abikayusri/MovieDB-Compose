plugins {
    alias(libs.plugins.app.library.compose)
}

android {
    namespace = "abika.sinau.moviedbnew.core.sharedui"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.ktor)
}
