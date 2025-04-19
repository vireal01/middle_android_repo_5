package com.vireal.buildsrc

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

abstract class FindUntranslatedStringsTask : DefaultTask() {
  @TaskAction
  fun findUntranslatedStrings() {
    val resDir = File(project.projectDir, "src/main/res")

    fun findAllValuesCatalogsWithStrings(): Map<File, String> {
      val valuesDirs = resDir.listFiles { file ->
        file.isDirectory && file.name.startsWith("values-")
      } ?: emptyArray()

      return valuesDirs.mapNotNull { dir ->
        val stringsFile = File(dir, "strings.xml")
        if (stringsFile.exists()) stringsFile to dir.name else null
      }.toMap()
    }

    fun getStringsFromXml(file: File): NodeList {
      return DocumentBuilderFactory
        .newInstance()
        .newDocumentBuilder()
        .parse(file)
        .getElementsByTagName("string")
    }

    fun getAllNamesFromStrings(nodeList: NodeList): List<String> {
      val names: MutableList<String> = mutableListOf()
      nodeList.let { nodeList ->
        (0 until nodeList.length).map { i ->
          val node = nodeList.item(i) // node - один узел из списка stringsFromXml.
          val name = node.attributes?.getNamedItem("name")?.nodeValue ?: "" // получаем атрибуты узла (в данном случае name).
          if (name.isNotEmpty()) {
            names.add(name)
          }
        }
      }
      return names.toList()
    }

    val baseStringsFile = File(resDir, "values/strings.xml")
    val missingStrings = mutableMapOf<File, MutableList<String>>()

    val baseStrings = getStringsFromXml(baseStringsFile)
    val originalStringNames = getAllNamesFromStrings(baseStrings)

    val allLocaleStrings: Map<File, String> = findAllValuesCatalogsWithStrings()
    allLocaleStrings.forEach { stringsFile ->
      val strings = getStringsFromXml(stringsFile.key)
      val names = getAllNamesFromStrings(strings)
      originalStringNames.forEach{ ogName ->
        if (!names.contains(ogName)) {
          if(missingStrings[stringsFile.key] != null) {
            missingStrings[stringsFile.key]?.add(ogName)
          } else {
            missingStrings[stringsFile.key] = mutableListOf(ogName)
          }
        }
      }
    }

    if (missingStrings.isNotEmpty()) {
      val stringBuilderErrorText = StringBuilder("Missing translations").append(System.lineSeparator())
      missingStrings.forEach { missing ->
        stringBuilderErrorText
          .append("=== ${missing.key} ===")
          .append(System.lineSeparator())
          .append(missing.value.joinToString(separator = System.lineSeparator()))
          .append(System.lineSeparator())
      }
      throw GradleException(stringBuilderErrorText.toString())
    }
  }
}
