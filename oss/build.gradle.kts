
plugins {
    kotlin("jvm") version "2.0.0"
//    id("me.champeau.jmh") version "0.7.1"
//    id("org.jetbrains.kotlin.plugin.allopen") version "2.0.0"

    id("org.jetbrains.kotlinx.benchmark") version "0.4.11" //kotlinx.benchmark
    kotlin("plugin.allopen") version "1.9.20"
}

group = "com.benchmark"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    // kotlin csv oss 비교군 1
    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0") // for JVM platform
    // json-csv-bridge oss 비교군 2
    implementation("com.github.hyunolike:json-csv-bridge:v1.0.0")
    implementation("com.jsoizo:kotlin-csv-jvm:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.11")

//    jmh("org.openjdk.jmh:jmh-core:1.36")
//    jmh("org.openjdk.jmh:jmh-generator-annprocess:1.36")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

//jmh {
//    jvmArgs.set(listOf("-Xmx4g")) // 메모리를 4GB로 증가
//}

sourceSets {
    val benchmark by creating {
        kotlin.srcDir("src/benchmark/kotlin")
        resources.srcDir("src/benchmark/resources")
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

benchmark {
    targets {
        register("benchmark")
    }
}

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}