/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10.2/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}


configurations.all {
    exclude(module = "slf4j-log4j12")
}


dependencies {
    // Use JUnit test framework.
    testImplementation(libs.junit)

    // This dependency is used by the application.
    implementation(libs.guava)

    implementation("org.reflections:reflections:0.10.2")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")

    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")

    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-api:1.7.25")

    implementation("org.slf4j:slf4j-log4j12:2.0.16")

    testImplementation("org.slf4j:slf4j-simple:2.0.16")

    runtimeOnly("ch.qos.logback:logback-classic:1.4.12")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "org.example.App"
}
