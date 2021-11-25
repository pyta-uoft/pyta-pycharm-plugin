import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun properties(key: String) = project.findProperty(key).toString()

plugins {
    // Java support
    id("java")
    // Kotlin support
    id("org.jetbrains.kotlin.jvm") version "1.5.30"
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.1.6"
    // Ktlint from repository - https://github.com/jlleitschuh/ktlint-gradle
    id("org.jlleitschuh.gradle.ktlint") version "10.2.0"
}

group = properties("pluginGroup")
version = properties("pluginVersion")

// Configure project's dependencies
repositories {
    mavenCentral()
}

dependencies {
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
}

// Do not accept publishing of build scan results. Can be changed to "yes" if needed in the future.
extensions.findByName("buildScan")?.withGroovyBuilder {
    setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
    setProperty("termsOfServiceAgree", "no")
}

// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    downloadSources.set(properties("platformDownloadSources").toBoolean())
    updateSinceUntilBuild.set(true)

    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}

ktlint {
    outputToConsole.set(true)
    outputColorName.set("RED")
}

tasks {
    // Use the native JUnit support of Gradle.
    "test"(Test::class) {
        useJUnitPlatform()
    }

    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = it
        }
    }

    wrapper {
        gradleVersion = properties("gradleVersion")
    }


    // Configure UI tests plugin
    // Read more: https://github.com/JetBrains/intellij-ui-test-robot
    runIdeForUiTests {
        systemProperty("robot-server.port", "8082")
        systemProperty("ide.mac.message.dialogs.as.sheets", "false")
        systemProperty("jb.privacy.policy.text", "<!--999.999-->")
        systemProperty("jb.consents.confirmation.enabled", "false")
    }

}
