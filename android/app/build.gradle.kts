import org.gradle.api.tasks.Exec

plugins {
    id("com.android.application")
}

val repositoryRoot = rootProject.projectDir.parentFile
val codeRoot = repositoryRoot.resolve("GeneralsMD/Code")
val sdlAndroidProject = codeRoot.resolve("Libraries/Source/SDL3/android-project/app/src/main")

// Third-party source archives are intentionally not committed. The repository's pinned vendor script
// restores SDL3 and the other required source trees before CMake configures.
val prepareVendoredSources by tasks.registering(Exec::class) {
    workingDir(repositoryRoot)
    commandLine("bash", "GeneralsMD/Code/Tools/vendor.sh")
    outputs.upToDateWhen { false }
}

tasks.configureEach {
    if (name.startsWith("preBuild") ||
        name.startsWith("configureCMake") ||
        name.startsWith("buildCMake") ||
        name.startsWith("compile")) {
        dependsOn(prepareVendoredSources)
    }
}

android {
    namespace = "org.libsdl.app"
    compileSdk = 35
    ndkVersion = "28.2.13676358" // Android NDK r28c (SDL3 3.4.16 baseline)

    defaultConfig {
        applicationId = "org.reforged.generals"
        minSdk = 28 // PosixInstallRoot currently uses glob/globfree, introduced in Android API 28.
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"

        ndk {
            abiFilters += listOf("arm64-v8a")
        }

        externalNativeBuild {
            cmake {
                arguments += listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_PLATFORM=android-28"
                )
                targets += listOf("generals")
            }
        }
    }

    sourceSets {
        getByName("main") {
            // SDLActivity is supplied by the same pinned SDL3 source tree that CMake builds.
            java.srcDir(sdlAndroidProject.resolve("java"))
            res.srcDir(sdlAndroidProject.resolve("res"))
        }
    }

    externalNativeBuild {
        cmake {
            path = file("../../GeneralsMD/Code/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    buildTypes {
        debug {
            isJniDebuggable = true
        }
        release {
            isMinifyEnabled = false
        }
    }

    packaging {
        jniLibs {
            useLegacyPackaging = false
        }
    }
}

dependencies {
}
