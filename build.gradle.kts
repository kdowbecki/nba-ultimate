plugins {
  id("org.jetbrains.kotlin.jvm").version("1.8.10")
  application
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.apache.commons:commons-math3:3.6.1")
  implementation("org.xerial:sqlite-jdbc:3.41.2.1")

  implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
  implementation("org.slf4j:slf4j-simple:2.0.7")

  testImplementation(kotlin("test"))
  testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.35.0")
}

tasks.test {
  useJUnitPlatform()
  testLogging {
    events("PASSED", "SKIPPED", "FAILED")
  }

  // TODO WireMock by default uses own self signed certificates
  systemProperty("jdk.internal.httpclient.disableHostnameVerification", "true")
  systemProperty("javax.net.ssl.trustStore", "src/test/resources/keystore")
}

application {
  mainClass.set("nba.ultimate.AppKt")
}
