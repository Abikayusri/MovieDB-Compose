plugins {
    alias(libs.plugins.app.library)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "abika.sinau.moviedbnew.core.network"
}

dependencies {
    api(project(":core:config"))
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.auth)
}
