import com.github.spotbugs.SpotBugsTask

plugins {
    id("nebula.dependency-lock") version "7.3.0"

    pmd
    // REVISIT: spotbugs classpath bs in 1.6.10
    // https://github.com/spotbugs/spotbugs-gradle-plugin/issues/119
    id("com.github.spotbugs") version "1.6.9"
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
    apply(plugin = "com.github.spotbugs")

    tasks.withType<Pmd>().configureEach {
        ignoreFailures = true
        ruleSetFiles = rootProject.files("config/pmd.xml")
        // ruleSets overrides ruleSetFiles if not empty. it's really stupid.
        ruleSets = listOf()
    }

    tasks.withType<SpotBugsTask>().configureEach {
        ignoreFailures = true
        // TODO: create spotbugs filter
//        includeFilter = rootProject.file("config/spotbugs.xml")
        reports {
            xml.isEnabled = false
            html.isEnabled = true
        }
    }
}
