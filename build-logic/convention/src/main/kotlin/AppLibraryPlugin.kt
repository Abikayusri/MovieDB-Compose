import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AppLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        // AGP 9.x bundles Kotlin support natively — do NOT apply kotlin.android separately,
        // it would re-register the 'kotlin' extension and cause a conflict.
        pluginManager.apply("com.android.library")

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        extensions.configure<LibraryExtension> {
            compileSdk = 36
            defaultConfig {
                minSdk = 24
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_11
                targetCompatibility = JavaVersion.VERSION_11
            }
        }

        dependencies {
            "implementation"(libs.findLibrary("androidx-core-ktx").get())
            "implementation"(libs.findLibrary("koin-android").get())
            "implementation"(libs.findLibrary("kotlinx-coroutines-android").get())
            "testImplementation"(libs.findLibrary("junit").get())
        }
    }
}
