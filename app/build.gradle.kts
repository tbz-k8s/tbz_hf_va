import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.sun.org.apache.bcel.internal.Repository
import groovy.lang.Closure

plugins {
    idea
    kotlin("jvm") version "1.3.50"
    id("com.github.johnrengelman.shadow") version "5.1.0"
    id("com.bmuschko.docker-remote-api") version "5.2.0"
    id("com.palantir.git-version") version "0.12.2"
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

// https://github.com/palantir/gradle-git-version/issues/105#issuecomment-523192407
val gitVersion: Closure<*> by extra
version = gitVersion()

val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "ch.nliechti.MainKt"
    }
}

dependencies {
    implementation("org.springframework:spring-web:5.0.2.RELEASE")
    implementation("io.javalin:javalin:3.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.0")
    implementation("org.slf4j:slf4j-simple:1.7.28")

    // Web stuff
    implementation("org.webjars.npm:vue:2.6.10")
    compile("org.webjars.npm:vuetify:2.1.1")

    implementation("io.fabric8:kubernetes-client:4.6.0")
    implementation("io.fabric8:kubernetes-model:4.6.0")
}

docker {
    registryCredentials {
        url.set("https://index.docker.io/v1/")
        username.set(System.getenv("DOCKER_USERNAME"))
        password.set(System.getenv("DOCKER_PASSWORD"))
        email.set("docker@nliechti.ch")
    }
}

// Use task types
tasks.register("buildDockerImage", DockerBuildImage::class) {
    dependsOn("shadowJar")
    
    inputDir.set(file("."))
    tags.add("nliechti/tbz_deployer:$version")
}
