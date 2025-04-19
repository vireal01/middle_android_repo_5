plugins {
  `kotlin-dsl`
}

dependencies {
  implementation(gradleApi())
}

repositories {
  google()
  mavenCentral()
}

gradlePlugin {
  plugins {
    create("findUntranslatedStringsPlugin") {
      id = "find_untransalted_strings_plugin"
      implementationClass = "com.vireal.buildsrc.FindUntranslatedStringsPlugin"
    }
  }
}