import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("xyz.jpenilla.run-paper") version "2.0.0"
}


group = "club.tesseract"
version = "1.0.0"

repositories {
    flatDir {
        dirs("$rootDir/libs")
    }
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io")
}


dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    implementation("com.charleskorn.kaml:kaml:0.49.0")
    implementation("com.github.TropicalShadow:FastInv-Adventure:3bca07ecec")
}

tasks{
    runServer {
        minecraftVersion("1.19.2")
    }
    shadowJar{
        archiveBaseName.set(project.name)
        archiveClassifier.set("")

        relocate("com.charleskorn.kaml", "club.tesseract.rpgteleporting.relocated.com.charleskorn.kaml")
        relocate("fr.mrmicky.fastinv", "club.tesseract.rpgteleporting.relocated.fr.mrmicky")
        relocate("org.jetbrains.kotlinx", "club.tesseract.rpgteleporting.relocated.org.jetbrains.kotlinx")
    }
    processResources {
        filter<org.apache.tools.ant.filters.ReplaceTokens>("tokens" to mapOf("version" to project.version))
    }
}


val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_16.toString()

compileKotlin.kotlinOptions {
    freeCompilerArgs = listOf("-Xinline-classes")
}