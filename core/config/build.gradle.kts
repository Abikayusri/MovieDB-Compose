plugins {
    alias(libs.plugins.app.library.compose)
}

android {
    namespace = "abika.sinau.moviedbnew.core.config"
}

dependencies {
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
