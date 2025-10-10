import java.nio.file.Files
import java.nio.file.Path
import java.io.File

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class WarframeCSV(
    val warframeId: Int,
    val name: String,
    val health: Int,
    val armor: Int,
    val energy: Int,
    val sprintSpeed: Double,
    val passive: String
)

fun leerDatosInicialesCSV(ruta: Path): List<WarframeCSV>
{
    var warframes: List<WarframeCSV> =emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else{

        val reader = csvReader {
            delimiter = ';'
        }

        val filas: List<List<String>> = reader.readAll(ruta.toFile())

        warframes = filas.mapNotNull { columnas ->

            if (columnas.size >= 5) {
                try {
                    val idWarframe = columnas[0].toInt()
                    val nombre = columnas[1]
                    val vida = columnas[2].toInt()
                    val armadura = columnas[3].toInt()
                    val energia = columnas[4].toInt()
                    val velocidadSprint = columnas[5].toDouble()
                    val pasiva = columnas[6]
                    WarframeCSV(idWarframe,
                        nombre,
                        vida,
                        armadura,
                        energia,
                        velocidadSprint,
                        pasiva)

                } catch (e: Exception) {

                    println("Fila inválida ignorada: $columnas -> Error: ${e.message}")
                    null
                }
            } else {

                println("Fila con formato incorrecto ignorada: $columnas")

                null
            }
        }
    }
    return warframes
}

fun escribirDatosCSV(ruta: Path, warframes: List<WarframeCSV>){
    try {
        val fichero: File = ruta.toFile()
        csvWriter {
            delimiter = ';'
        }.writeAll(
            warframes.map { warframe ->
                listOf(warframe.warframeId.toString(),
                    warframe.name,
                    warframe.health.toString(),
                    warframe.armor.toString(),
                    warframe.energy.toString(),
                    warframe.sprintSpeed.toString(),
                    warframe.passive)
            },
            fichero
        )
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun ejecutarCSV() {
    val entradaCSV = Path.of("game_data/CSV/Warframes.csv")
    val salidaCSV = Path.of("game_data/CSV/Warframes2.csv")
    val datos = leerDatosInicialesCSV(entradaCSV)
    for (dato in datos) {
        println(" - ID: ${dato.warframeId}," +
                " Nombre: ${dato.name}," +
                " Vida: ${dato.health}," +
                " Armadura: ${dato.armor}," +
                " Energía: ${dato.energy}," +
                " Velocidad de sprint: ${dato.sprintSpeed}," +
                " Pasiva: ${dato.passive}")
    }
    escribirDatosCSV(salidaCSV, datos)
}
