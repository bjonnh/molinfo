plugins {
    kotlin("jvm")
    application
    // kotlin("plugin.serialization")
    id("com.github.ben-manes.versions")
    id("maven-publish")
    id("com.github.johnrengelman.shadow")
    id("org.jetbrains.dokka")
    id("fr.coppernic.versioning")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jmailen.kotlinter")
    id("org.jetbrains.changelog")
}

fun properties(key: String) = project.findProperty(key).toString()
val projectVersion: String by project
val publicationName: String by project
group = "net.nprod"
version = projectVersion + if (System.getProperty("snapshot")?.isEmpty() != false) {
    ""
} else {
    "-SNAPSHOT"
}

repositories {
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx")
    maven("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
}

dependencies {
    // val serializationRuntimeVersion: String by project
    val kotlinLoggingVersion: String by project
    val junitApiVersion: String by project
    val cdkVersion: String by project
    val ktorVersion: String by project
    val logbackVersion: String by project
    val redissonVersion: String by project

    implementation(kotlin("stdlib"))
    // implementation(kotlin("reflect"))
    // implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", serializationRuntimeVersion)

    implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion") {
        exclude("org.slf4j")
    }

    implementation("org.openscience.cdk:cdk-bundle:$cdkVersion") {
        exclude("joda-time")
    }

    // ktor

    implementation("io.ktor:ktor-server-cio:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-metrics:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")

    // caching

    implementation("org.redisson:redisson:$redissonVersion")

    // tests

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitApiVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitApiVersion")
}

/**
 * The compatibility section (minimal version of Java compatible)
 */

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "11"
        kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "11"
    }

    dokkaHtml.configure {
        outputDirectory.set(buildDir.resolve("dokka"))
    }
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

/*val javadocJar by tasks.registering(Jar::class) {
    dependsOn("dokkaH")
    archiveClassifier.set("javadoc")
    from("$buildDir/dokka")
}*/

publishing {
    publications {
        create<MavenPublication>(publicationName) {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            from(components["java"])
            artifact(sourcesJar.get())
            // artifact(javadocJar.get())
        }
    }
}

/**
 * Configuration of test framework
 */

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// Quality

ktlint {
    filter {
        exclude { element -> element.file.path.contains("generated/") }
        include("**/kotlin/**")
    }
}

kotlinter {
    ignoreFailures = project.hasProperty("lintContinueOnError")
    experimentalRules = project.hasProperty("lintKotlinExperimental")
}

detekt {
    val detektVersion: String by project
    toolVersion = detektVersion
    config = rootProject.files("qc/detekt.yml")
    buildUponDefaultConfig = true
    baseline = rootProject.file("qc/detekt-baseline.xml")
}

// Documentation

changelog {
    version = projectVersion
    groups = listOf("Added", "Changed", "Deprecated", "Removed", "Fixed", "Security")
}

sourceSets["main"].resources.srcDirs("resources")

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}
