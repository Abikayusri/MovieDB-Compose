import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AppFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.library.compose")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            // Core module deps auto-included in all feature modules
            "implementation"(project(":core:config"))
            "implementation"(project(":core:design-system"))
            "implementation"(project(":core:shared-ui"))
            "implementation"(project(":core:shared-model"))
            "implementation"(project(":core:navigation"))
            "implementation"(project(":core:network"))
            // Feature-specific libs
            "implementation"(libs.findLibrary("koin-compose-viewmodel").get())
            "implementation"(libs.findLibrary("navigation3-ui").get())
            "implementation"(libs.findLibrary("coil-compose").get())
            "implementation"(libs.findLibrary("coil-network-ktor").get())
            "implementation"(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
        }
    }
}
