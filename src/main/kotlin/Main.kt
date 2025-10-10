package org.example

import ejecutarCSV
import ejecutarConversion
import ejecutarJSON
import ejecutarXML
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val carpetaWarframes = Path.of("game_data/Warframes")
    val carpetaArmas = Path.of("game_data/Weapons")
    val carpetaXML = Path.of("game_data/XML")
    val carpetaJSON = Path.of("game_data/JSON")
    val carpetaCSV = Path.of("game_data/CSV")
    val carpetaConversions = Path.of("game_data/Conversions")

    val ficheroNombres = carpetaWarframes.resolve("Warframe_names.txt")
    val placeholder = carpetaArmas.resolve(".gitkeep")

    try {
        // Crear directorios si no existen
        listOf(
            carpetaWarframes, carpetaArmas, carpetaXML, carpetaJSON,
            carpetaCSV, carpetaConversions
        ).forEach {
            if (Files.notExists(it)) Files.createDirectories(it)
        }

        // Verificar archivo de nombres
        if (Files.notExists(ficheroNombres)) {
            println("No se ha encontrado el fichero: ${ficheroNombres.fileName}")
        } else {
            println("Encontrado el fichero: ${ficheroNombres.fileName}")
        }

        // Crear placeholder si falta
        if (Files.notExists(placeholder)) Files.createFile(placeholder)

        println("\n=== Lectura y escritura XML ===")
        ejecutarXML()

        println("\n=== Lectura y escritura JSON ===")
        ejecutarJSON()

        println("\n=== Lectura y escritura CSV ===")
        ejecutarCSV()

        println("\n=== Conversión JSON → XML ===")
        ejecutarConversion()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
