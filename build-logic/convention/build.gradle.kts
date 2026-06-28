plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("appLibrary") {
            id = "app.library"
            implementationClass = "AppLibraryPlugin"
        }
        register("appLibraryCompose") {
            id = "app.library.compose"
            implementationClass = "AppLibraryComposePlugin"
        }
        register("appFeature") {
            id = "app.feature"
            implementationClass = "AppFeaturePlugin"
        }
        register("appDomain") {
            id = "app.domain"
            implementationClass = "AppDomainPlugin"
        }
        register("appData") {
            id = "app.data"
            implementationClass = "AppDataPlugin"
        }
    }
}
