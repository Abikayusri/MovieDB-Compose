import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class AppDomainPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("app.library")

        dependencies {
            "implementation"(project(":core:config"))
            "implementation"(project(":core:shared-model"))
            "implementation"(project(":core:shared-data-store"))
        }
    }
}
