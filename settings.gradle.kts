pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Tally"
include(":app")
include(":app:tallyQrGenerator")
include(":app:coreMechanism")
include(":app:tallyQrGeneratorUI")
