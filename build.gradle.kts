plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("ch.qos.logback:logback-core:1.4.11")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.compileJava {
    options.compilerArgs.add("--add-exports")
    options.compilerArgs.add("java.base/sun.nio.ch=ALL-UNNAMED")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED")
}