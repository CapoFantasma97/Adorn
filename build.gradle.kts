import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.6.10"
    id("dev.architectury.loom") version "0.11.0-SNAPSHOT"
    `maven-publish`
    id("org.jmailen.kotlinter") version "3.2.0"
}

group = "io.github.juuxel"
version = "${project.property("mod-version")}+${project.property("minecraft-version")}-forge"

base {
    archivesName.set("Adorn")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withSourcesJar()
}

loom {
    forge {
        mixinConfig("mixins.adorn.json")
    }

    runConfigs.configureEach {
        val capitalizedName = if (name.length <= 1) name else name[0].toUpperCase() + name.substring(1)
        configName = "Forge $capitalizedName"
    }
}

repositories {
    maven {
        name = "kotlinforforge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
}

dependencies {
    /**
     * Gets a version string with the [key].
     */
    fun v(key: String) = project.property(key).toString()

    minecraft("com.mojang:minecraft:${v("minecraft-version")}")
    mappings("net.fabricmc:yarn:" + v("minecraft-version") + '+' + v("mappings") + ":v2")

    // Forge
    forge("net.minecraftforge", "forge", v("forge"))
    implementation("thedarkcolour", "kotlinforforge", v("kotlin-for-forge"))
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand("version" to project.version)
        }
    }
}

kotlinter {
    disabledRules = arrayOf("parameter-list-wrapping")
}
