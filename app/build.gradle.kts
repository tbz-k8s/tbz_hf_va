import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure

plugins {
    idea
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("com.bmuschko.docker-remote-api") version "5.2.0"
    id("com.palantir.git-version") version "0.12.2"
    id("org.flywaydb.flyway") version "6.0.6"
}

buildscript {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://dl.bintray.com/kotlin/exposed")
        }
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.50"))
    }
}

repositories {
    mavenCentral()
}

// https://github.com/palantir/gradle-git-version/issues/105#issuecomment-523192407
val gitVersion: Closure<*> by extra
version = gitVersion()

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "ch.nliechti.MainKt"
    }
}

dependencies {
    implementation("io.javalin:javalin:3.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0")
    implementation("org.slf4j:slf4j-simple:1.7.28")

    // Web stuff
    implementation("org.webjars.npm:vue:2.6.10")
    compile("org.webjars.npm:vuetify:2.1.1")

    implementation("io.fabric8:kubernetes-client:4.6.0")
    implementation("io.fabric8:kubernetes-model:4.6.0")

    // Database
//    implementation("org.jetbrains.exposed:exposed:0.14.1")
    implementation("com.h2database:h2:1.4.199")
    implementation("org.flywaydb:flyway-core:6.0.6")
}

tasks.withType<ShadowJar> {
    version = null
}

docker {
    registryCredentials {
        url.set("https://index.docker.io/v1/")
        username.set(System.getenv("DOCKER_USERNAME"))
        password.set(System.getenv("DOCKER_PASSWORD"))
        email.set("docker@nliechti.ch")
    }
}

val dockerImageName = "nliechti/tbz_deployer"

// Use task types
tasks.create("buildDockerImage", DockerBuildImage::class) {
    dependsOn("shadowJar")
    
    inputDir.set(file("."))
    if(!version.toString().contains("dirty")) {
       tags.add("$dockerImageName:$version")
    }
    tags.add("$dockerImageName:latest")
}

tasks.create("pushDockerImage", DockerPushImage::class) {
    dependsOn("buildDockerImage")
    imageName.set("$dockerImageName:$version")
}

tasks.create("pushLatestDockerImage", DockerPushImage::class) {
    dependsOn("buildDockerImage")
    imageName.set("$dockerImageName:latest")
}

tasks.create("pushVersionedDockerImage", DockerPushImage::class) {
    dependsOn("buildDockerImage")
    imageName.set("$dockerImageName:$version")
}

tasks.create("pushDockerImages") {
    dependsOn("pushVersionedDockerImage")
    dependsOn("pushLatestDockerImage")
}

