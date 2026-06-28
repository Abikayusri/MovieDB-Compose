plugins {
    alias(libs.plugins.app.library)
}

android {
    namespace = "abika.sinau.moviedbnew.core.shareddatastore"
}

dependencies {
    implementation(libs.datastore.preferences)
    implementation(project(":core:shared-model"))
}
