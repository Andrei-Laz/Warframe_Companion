import java.nio.file.Path

fun ejecutarConversion() {
    val entradaJSON = Path.of("game_data/JSON/Warframes.json")
    val salidaXML = Path.of("game_data/Conversions/JSON_to_XML.xml")

    val datosJson = leerDatosInicialesJSON(entradaJSON)

    val datosXml = datosJson.map {
        WarframeXML(
            warframeId = it.warframeId,
            nombre = it.name,
            vida = it.health,
            armadura = it.armor,
            energia = it.energy,
            velocidadSprint = it.sprintSpeed,
            pasiva = it.passive
        )
    }

    escribirDatosXML(salidaXML, datosXml)
    println("Conversión JSON → XML completada.")
}
