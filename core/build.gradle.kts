plugins {
    id("java")
}

repositories {
    mavenCentral()
}

dependencies {
    api(project(":api"))
    api("me.sunlan:fast-reflection:1.0.3")
}