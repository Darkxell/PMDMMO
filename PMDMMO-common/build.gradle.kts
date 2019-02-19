plugins {
    java
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
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.4")
    implementation("org.jdom:jdom2:2.0.6")

    testImplementation("org.junit.jupiter:junit-jupiter:5.4.0")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}
