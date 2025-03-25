plugins {
    id("com.gtnewhorizons.gtnhconvention")
    id("org.jetbrains.kotlin.jvm")
}

// Shadow source jars too
val shadowSources = configurations.getByName("shadowSources")
tasks.sourcesJar {
    from(shadowSources.map { zipTree(it) })
}
