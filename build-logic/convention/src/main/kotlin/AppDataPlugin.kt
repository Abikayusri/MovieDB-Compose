import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

class AppDataPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.library")
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("androidx.room")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        dependencies {
            "implementation"(project(":core:network"))
            "implementation"(project(":core:shared-data-store"))
            "implementation"(project(":core:shared-model"))
            "implementation"(libs.findLibrary("kotlinx-serialization-json").get())
            "implementation"(libs.findLibrary("room-runtime").get())
            "implementation"(libs.findLibrary("room-ktx").get())
            "ksp"(libs.findLibrary("room-compiler").get())
            "implementation"(libs.findLibrary("datastore-preferences").get())
            "implementation"(libs.findLibrary("ktor-client-core").get())
            "implementation"(libs.findLibrary("ktor-client-android").get())
            "implementation"(libs.findLibrary("ktor-client-content-negotiation").get())
            "implementation"(libs.findLibrary("ktor-serialization-kotlinx-json").get())
            "implementation"(libs.findLibrary("ktor-client-logging").get())
        }
    }
}
