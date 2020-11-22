import com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA

plugins {
    java
    id("com.adarshr.test-logger") version "2.1.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("io.vertx:vertx-pg-client:3.9.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    testImplementation("org.testcontainers:testcontainers:1.15.0")
    testImplementation("org.testcontainers:junit-jupiter:1.15.0")
}

tasks.test {
    useJUnitPlatform()
    reports.html.isEnabled = false
}

testlogger {
    theme = MOCHA
    showStandardStreams = true
    showFullStackTraces = true
}
