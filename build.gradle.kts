plugins {
    id("com.gtnewhorizons.gtnhconvention")
    id("org.jetbrains.kotlin.jvm")
    id("com.gradleup.shadow")
}

// dependencies in shadowImplementation will be bundled in the jar without classifier,
// and they'll also be accessible in projects that depends on this.
val shadowImplementation =
    configurations.create("shadowImplementation") {
        isCanBeConsumed = false
        isCanBeResolved = true
    }
configurations.api.configure { extendsFrom(shadowImplementation) }

dependencies {
    // Bundled
    shadowImplementation(libs.annotations)
    shadowImplementation(libs.kotlin.stdlib)
    shadowImplementation(libs.kotlin.stdlib.jdk8)
    shadowImplementation(libs.kotlin.reflect)
    shadowImplementation(libs.kotlinx.coroutines.core)
    shadowImplementation(libs.kotlinx.coroutines.jdk8)

    // MC related
    compileOnly("com.github.GTNewHorizons:GTNHLib:0.6.38:dev")
}

tasks.jar {
    archiveClassifier = null
}

tasks.shadowJar {
    configurations = listOf(shadowImplementation)
    archiveClassifier = "shadow" // not obfuscated
}

tasks.reobfJar {
    inputJar = tasks.shadowJar.flatMap { it.archiveFile }
    archiveClassifier = "prod" // final result for production
}
