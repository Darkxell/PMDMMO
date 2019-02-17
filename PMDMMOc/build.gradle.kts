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

    implementation("com.github.Vatuu:discord-rpc:1.5.0")
    implementation("javax.websocket:javax.websocket-api:1.0")
    implementation("com.googlecode.soundlibs:mp3spi:1.9.5.4")
    implementation("com.googlecode.soundlibs:tritonus-share:0.3.7.4")
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.4")
    implementation("org.jdom:jdom2:2.0.6")
}

application {
    mainClassName = "com.darkxell.client.launchable.Launcher"
}
