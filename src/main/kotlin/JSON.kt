import java.nio.file.Files
import java.nio.file.Path
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class WarframeJSON(
    @SerialName("warframe_id")
    val warframeId: Int,
    val name: String,
    val health: Int,
    val armor: Int,
    val energy: Int,
    @SerialName("sprint_speed")
    val sprintSpeed: Double,
    val passive: String
)

fun leerDatosInicialesJSON(ruta: Path): List<WarframeJSON> {
    var warframes: List<WarframeJSON> = emptyList()
    val jsonString = Files.readString(ruta)

    warframes = Json.decodeFromString<List<WarframeJSON>>(jsonString)
    return warframes
}

fun escribirDatosJSON(ruta: Path, warframes: List<WarframeJSON>) {
    try {
        val json = Json { prettyPrint = true }.encodeToString(warframes)

        Files.writeString(ruta, json)
        println("\nInformación guardada en: $ruta")
    } catch (e: Exception) {
        println("Error: ${e.message}")
        e.printStackTrace()
    }
}

fun main() {
    val entradaJSON = Path.of("game_data/JSON/Warframes.json")
    val salidaJSON = Path.of("game_data/JSON/Warframes2.json")
    val datos: List<WarframeJSON>
    datos = leerDatosInicialesJSON(entradaJSON)
    for (dato in datos) {
        println(" - ID: ${dato.warframeId}," +
                " Nombre: ${dato.name}," +
                " Vida: ${dato.health}," +
                " Armadura: ${dato.armor}," +
                " Energía: ${dato.energy}" +
                " Velocidad de sprint: ${dato.sprintSpeed}" +
                " Pasiva: ${dato.passive}")
    }
    escribirDatosJSON(salidaJSON, datos)
}