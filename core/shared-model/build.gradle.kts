plugins {
    alias(libs.plugins.app.library)
}

android {
    namespace = "abika.sinau.moviedbnew.core.sharedmodel"
}

// shared-model has ZERO inter-module dependencies — standalone domain types only.
// DO NOT add project() dependencies here.
