plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }

    maven {
        url = uri("https://mvn.lumine.io/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }

    maven {
        url = uri("https://maven.citizensnpcs.co/repo")
    }
}

dependencies {
    implementation("io.papermc.paper:paper-api:${project.properties["craftbukkit.version"]}")
    implementation("com.denizenscript:denizen:${project.properties["denizen.version"]}")
}

group = "com.codylamer"
version = "1.0.0"
description = "Zippezin"
java.sourceCompatibility = JavaVersion.VERSION_17

fun buildNumber(): String = System.getenv("BUILD_NUMBER") ?: "UNKNOWN"

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand(mapOf("BUILD_NUMBER" to System.getenv("BUILD_NUMBER")))
    }
}
