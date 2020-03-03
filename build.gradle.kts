import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    application
    kotlin("jvm") version "1.3.61"
}

group = "org.example"
version = "1.0-SNAPSHOT"

application {
    mainClassName = "Application"
}

repositories {
    mavenCentral()
}

dependencies {
    testCompile("junit", "junit", "4.12")
    compile("com.google.zxing", "core", "3.3.0")
    compile("org.apache.pdfbox", "pdfbox", "2.0.19")
    compile("com.google.zxing", "javase", "3.3.0")
    implementation(kotlin("stdlib-jdk8"))
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "Application"
    }
}