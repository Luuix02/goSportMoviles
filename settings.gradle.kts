pluginManagement {
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
    plugins {
        id("com.android.application") version "8.1.1" apply false
        id("org.jetbrains.kotlin.android") version "1.8.10" apply false
        id("androidx.navigation.safeargs.kotlin") version "2.7.0" apply false
        id("kotlin-parcelize") version "1.8.10" apply false
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains.kotlin.android)
//        alias(libs.plugins.jetbrains-kotlin.android)
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "goSportApp"
include(":app")
 