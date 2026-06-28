pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieDBNew"

include(":app")

// Core modules
include(":core:common")
include(":core:config")
include(":core:design-system")
include(":core:navigation")
include(":core:network")
include(":core:shared-data-store")
include(":core:shared-model")
include(":core:shared-ui")

// Domain, data, and feature modules — add as features are developed:
// include(":domain:movie")
// include(":data:movie")
// include(":feature:home")
// include(":feature:detail")
