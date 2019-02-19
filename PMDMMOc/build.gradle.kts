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
        resources.srcDir("test-resources")
    }
}

dependencies {
    compile(project(":PMDMMO-common"))

    implementation("com.github.Vatuu:discord-rpc:1.5.+")
    implementation("javax.websocket:javax.websocket-api:1.0")
    implementation("com.googlecode.soundlibs:mp3spi:1.9.+")
    implementation("com.googlecode.soundlibs:tritonus-share:0.3.+")
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.+")
    implementation("org.jdom:jdom2:2.0.+")

    testImplementation("org.junit.jupiter:junit-jupiter:5.4.+")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}

application {
    mainClassName = "com.darkxell.client.launchable.Launcher"
}
