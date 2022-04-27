import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
    kotlin("jvm") version "1.6.20"
//    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("com.google.protobuf") version "0.8.18"
    // so IntelliJ will find generated protobuf classes (in build/generated/source/proto)
    idea
    id("distribution")
    application
}

group = "com.kanekto"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val protobufVersion by extra("3.19.4")

sourceSets {
    main {
        // add protobuf files
        proto {
            srcDir("src/main/proto")
        }
    }
}

protobuf {
    // use published protoc executable (don't even use any locally installed protoc)
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    // also generate kotlin DSLs
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.builtins {
                id("kotlin")
            }
        }
    }
}

dependencies {
    // ktor
    implementation("io.ktor:ktor-server-core:2.0.0")
    implementation("io.ktor:ktor-server-netty:2.0.0")
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("io.ktor:ktor-server-content-negotiation:2.0.0")
    implementation("io.ktor:ktor-serialization-gson:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")

    // lambda
    implementation("com.amazonaws:aws-lambda-java-core:1.2.1")
    implementation("com.amazonaws:aws-lambda-java-events:3.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2")

    // cdk
    implementation("software.amazon.awscdk:core:1.137.0")
    implementation("software.amazon.awscdk:lambda:1.137.0")
    implementation("software.amazon.awscdk:aws-cdk-lib:2.20.0")
//    implementation("software.constructs:constructs:10.0.13")

    // protobuf
    api("com.google.protobuf:protobuf-java:$protobufVersion")
    api("com.google.protobuf:protobuf-java-util:$protobufVersion")
    api("com.google.protobuf:protobuf-kotlin:$protobufVersion")

    // database
    implementation("org.postgresql:postgresql:42.3.4")
    implementation("org.ktorm:ktorm-core:3.4.1")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Copy> {
    filesMatching("**/*.proto") {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.withType<KotlinCompile> {
    // generated protobuf classes make use of this
    kotlinOptions.freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    // LocalStack can only do up to Java 11
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11
}

//task<Exec>("deploy") {
//    dependsOn("shadowJar")
//    commandLine("serverless", "deploy")
//}
//
//task("cdk", JavaExec::class) {
////    dependsOn("shadowJar")
//    main = "provisioning.CdkKt"
//    classpath = sourceSets["main"].runtimeClasspath
//}
