plugins {
    java
    application
}

sourceSets {
    main {
        java.srcDir("src")
        resources.srcDir("resources")
    }
    test {
        java.srcDir("test")
    }
}

dependencies {
    compile(project(":PMDMMO-common"))
    compile(project(":PMDMMOc"))

    implementation("org.controlsfx:controlsfx:8.40.+")
    implementation("org.controlsfx:fxsampler:1.0.+")
    implementation("org.jdom:jdom2:2.0.+")
}

application {
    mainClassName = "fr.darkxell.dataeditor.application.DataEditor"
}
