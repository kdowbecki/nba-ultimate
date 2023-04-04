plugins {
  kotlin("jvm") version "1.8.10"
  kotlin("plugin.serialization") version "1.8.10"
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("org.apache.commons:commons-math3:3.6.1")
  implementation("org.xerial:sqlite-jdbc:3.41.2.1")

  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
  implementation("ch.qos.logback:logback-classic:1.4.6")

  testImplementation(kotlin("test"))
  testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
  testImplementation("io.mockk:mockk:1.13.4")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("PASSED", "SKIPPED", "FAILED")
  }

  // TODO How to load WireMock self-signed certificates?
  systemProperty("jdk.internal.httpclient.disableHostnameVerification", "true")
  systemProperty("javax.net.ssl.trustStore", "src/test/resources/keystore")
}

application {
  mainClass.set("nba.ultimate.AppKt")
}
