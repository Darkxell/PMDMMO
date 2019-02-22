plugins {
    id("nebula.dependency-lock") version "7.3.0"

    pmd
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
        maven("https://jitpack.io")
    }
}

subprojects {
    version = "0.1.0-alpha"

    apply(plugin = "pmd")

    tasks.withType<Pmd>().configureEach {
        ignoreFailures = true
        ruleSetFiles = rootProject.files("config/pmd.xml")
        // ruleSets overrides ruleSetFiles if not empty. it's really stupid.
        ruleSets = listOf()
    }
}
