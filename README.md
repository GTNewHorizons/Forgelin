# GTNH's Forgelin
Fork of [shadowfacts Forgelin](https://github.com/shadowfacts/Forgelin).

## Additions
- Backport to 1.7.10
- Shades the Kotlin standard library, runtime, coroutines-core, and reflect libraries so you don't have to.
- Provides a Forge `ILanguageAdapter` for using Kotlin `object` classes as your main mod class.

## Usage
```groovy
repositories {
    mavenCentral()
    maven {
        name = "jitpack"
        url = "https://jitpack.io"
    }
}

dependencies {
    compile "com.github.GTNewHorizons:Forgelin:LATEST_VERSION"
}
```

All versions can be seen [here](https://jitpack.io/#GTNewHorizons/Forgelin).

**Note:** You must have the `mavenCentral()` call in your `repositories` block. MavenCentral is used to host the Kotlin coroutines libraries.
