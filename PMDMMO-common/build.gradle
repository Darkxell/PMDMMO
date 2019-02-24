plugins {
    `java-library`
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
    implementation("com.eclipsesource.minimal-json:minimal-json:0.9.+")
    implementation("org.jdom:jdom2:2.0.+")

    testImplementation("org.junit.jupiter:junit-jupiter:5.4.+")
}

tasks {
    "test"(Test::class) {
        useJUnitPlatform()
    }
}
