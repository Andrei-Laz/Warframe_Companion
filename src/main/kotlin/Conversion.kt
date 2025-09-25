import java.nio.file.Path

fun convertJsonToXml (originRoute: Path, conversionRoute) {
    val data: List<WarframeJSON>
    data = leerDatosInicialesJSON(originRoute)

    escribirDatosXML(conversionRoute, )
}
fun main() {
    val entradaJSON = Path.of("game_data/JSON/Warframes.json")
    val salidaJSON = Path.of("game_data/Conversions/JSON_to_XML.xml")
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