pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {url = uri("https://www.jitpack.io" ) }

    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {url = uri("https://www.jitpack.io" ) }
        maven { url = uri ("https://storage.zego.im/maven") }   // <- Add this line.

    }
}

rootProject.name = "Facebook Clone"
include(":app")
 