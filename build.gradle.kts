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

    implementation("net.openhft:chronicle-queue:5.27ea2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.16.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.1")
}

tasks.compileJava {
    options.compilerArgs.add("--add-exports")
    options.compilerArgs.add("java.base/sun.nio.ch=ALL-UNNAMED")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs(
        "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-exports", "java.base/jdk.internal.ref=ALL-UNNAMED",
        "--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED",
        "--add-exports", "jdk.unsupported/sun.misc=ALL-UNNAMED",
        "--add-exports", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
        "--add-opens", "jdk.compiler/com.sun.tools.javac=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens", "java.base/java.io=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED"
    )
}