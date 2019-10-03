import com.sun.org.apache.bcel.internal.Repository

plugins {
    idea
    kotlin("jvm") version "1.3.50"
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.50"))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-web:5.0.2.RELEASE")
    implementation("io.javalin:javalin:3.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0")
    implementation("org.slf4j:slf4j-simple:1.7.28")
    implementation("org.webjars.npm:vue:2.6.10")
}