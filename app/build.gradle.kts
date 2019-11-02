import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.50"

plugins {
    idea
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("com.bmuschko.docker-remote-api") version "5.2.0"
    id("com.palantir.git-version") version "0.12.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.50"
}

buildscript {
    repositories {
        mavenCentral()
        jcenter()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.50"))
    }

}

repositories {
    mavenCentral()
    jcenter()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
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
    implementation("org.slf4j:slf4j-simple:1.7.28")

    // Web stuff
    implementation("org.webjars.npm:vue:2.6.10")
    implementation("org.webjars.npm:bootstrap-vue:2.0.4")
    implementation("org.webjars.npm:axios:0.19.0")

    implementation("io.fabric8:kubernetes-client:4.6.1")
    implementation("io.fabric8:kubernetes-model:4.6.1")

    // Database
    implementation("org.dizitart:potassium-nitrite:3.2.0")

    // Serializer
    implementation("javax.xml.bind:jaxb-api:2.3.1")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.9.5")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.9.5")

    constraints {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.5") {
            because("https://github.com/dizitart/nitrite-database/issues/164: Nitrite needs this version")
        }
    }

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
    if (!version.toString().contains("dirty")) {
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

