package org.example
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    val carpetaPersonajes = Path.of("game_data/Warframes")
    val carpetaArmas = Path.of("game_data/Weapons")

    //  val ficheroNombres = Path.of("Players/Player_Names.txt")
    val ficheroNombres = Path.of("game_data/Warframes", "Warframe_names.txt")

    val placeholder = Path.of("game_data/Weapons/.gitkeep")

    try {
        if (Files.notExists(carpetaArmas)) {
            Files.createDirectories(carpetaArmas)
        }
        if (Files.notExists(carpetaPersonajes)) {
            Files.createDirectories(carpetaPersonajes)
        }

        if (Files.notExists(ficheroNombres)) {
            println("No se ha encontrado el fichero con el nombre: " + ficheroNombres.fileName.toString())
        }
        else {
            println("Encontrado el fichero con el nombre: " + ficheroNombres.fileName)
        }

        if (Files.notExists(placeholder)) {
            Files.createFile(placeholder)
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }
}