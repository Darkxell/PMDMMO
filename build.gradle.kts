plugins {
    id("nebula.dependency-lock") version "7.3.0"
}

allprojects {
    repositories {
        jcenter()
        maven("https://jitpack.io")
    }
}

subprojects {
    version = "0.1.0-alpha"
}
