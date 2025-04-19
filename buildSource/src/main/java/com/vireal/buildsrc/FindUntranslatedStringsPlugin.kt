package com.vireal.buildsrc

import org.gradle.api.Plugin
import org.gradle.api.Project

class FindUntranslatedStringsPlugin : Plugin<Project> {
  override fun apply(project: Project) {
    project.tasks.register("untranslatedStrings", FindUntranslatedStringsTask::class.java)
  }
}