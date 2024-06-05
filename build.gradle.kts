plugins {
    kotlin("jvm") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.zenmo"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://repo.osgeo.org/repository/release/")
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    // I tried WFS-NG but it's cumbersome and HTTP redirects are not followed.
//    implementation("org.geotools:gt-wfs-ng:31.0")
    implementation("org.geotools:gt-api:31.0")
    implementation("org.geotools:gt-geojson-core:31.0")
    implementation("org.geotools:gt-main:31.0")

    // This is just for building URLS
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}
