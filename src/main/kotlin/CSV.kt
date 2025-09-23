import java.nio.file.Files
import java.nio.file.Path
import java.io.File

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class Planta(val id_planta: Int, val nombre_comun: String, val nombre_cientifico: String, val riego: Int, val altura: Double)

fun main() {
    val entradaCSV = Path.of("game_data/CSV/Warframes.csv")
    val salidaCSV = Path.of("documentos/mis_plantas2.csv")
    val datos: List<Planta>
    datos = leerDatosInicialesCSV(entradaCSV)
    for (dato in datos) {

        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, Nombre científico: ${dato.nombre_cientifico}, Frecuencia de riego: ${dato.riego} días, Altura: ${dato.altura} metros")
    }
    escribirDatosCSV(salidaCSV, datos)
}

fun leerDatosInicialesCSV(ruta: Path): List<Planta>
{
    var plantas: List<Planta> =emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else{

        val reader = csvReader {
            delimiter = ';'
        }

        val filas: List<List<String>> = reader.readAll(ruta.toFile())

        plantas = filas.mapNotNull { columnas ->

            if (columnas.size >= 5) {
                try {
                    val id_planta = columnas[0].toInt()
                    val nombre_comun = columnas[1]
                    val nombre_cientifico = columnas[2]
                    val riego = columnas[3].toInt()
                    val altura = columnas[4].toDouble()
                    Planta(id_planta,nombre_comun, nombre_cientifico, riego, altura)

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
    return plantas
}

fun escribirDatosCSV(ruta: Path,plantas: List<Planta>){
    try {
        val fichero: File = ruta.toFile()
        csvWriter {
            delimiter = ';'
        }.writeAll(
            plantas.map { planta ->
                listOf(planta.id_planta.toString(),
                    planta.nombre_comun,
                    planta.nombre_cientifico,
                    planta.riego.toString(),
                    planta.altura.toString())
            },
            fichero
        )
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}