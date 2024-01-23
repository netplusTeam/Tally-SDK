pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        //mavenCentral { uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //mavenCentral { uri("https://jitpack.io") }
    }
}

rootProject.name = "Tally"
include(":app")
include(":app:tallyQrGenerator")
include(":app:coreMechanism")
include(":app:tallyQrGeneratorUI")
