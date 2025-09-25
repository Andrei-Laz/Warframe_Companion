import java.nio.file.Path
import java.io.File
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

data class WarframeXML(
    @JacksonXmlProperty(localName = "warframe_id")
    val warframeId: Int,
    @JacksonXmlProperty(localName = "name")
    val nombre: String,
    @JacksonXmlProperty(localName = "health")
    val vida: Int,
    @JacksonXmlProperty(localName = "armor")
    val armadura: Int,
    @JacksonXmlProperty(localName = "energy")
    val energia: Int,
    @JacksonXmlProperty(localName = "sprint_speed")
    val velocidadSprint: Double,
    @JacksonXmlProperty(localName = "passive")
    val pasiva: String
)

@JacksonXmlRootElement(localName = "warframes")

data class Warframes(
    @JacksonXmlElementWrapper(useWrapping = false)
@JacksonXmlProperty(localName = "warframe")
val listaWarframes: List<WarframeXML> = emptyList()
)

fun leerDatosInicialesXML(ruta: Path): List<WarframeXML> {

    val fichero: File = ruta.toFile()

    val xmlMapper = XmlMapper().registerKotlinModule()

    val plantasWrapper: Warframes = xmlMapper.readValue(fichero)
    return plantasWrapper.listaWarframes
}

fun escribirDatosXML(ruta: Path, warframes: List<WarframeXML>) {
    try {
        val fichero: File = ruta.toFile()

        val contenedorXml = Warframes(warframes)

        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString =
            xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedorXml)

        fichero.writeText(xmlString)
        println("\nInformación guardada en: $fichero")
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}

fun main() {
    val entradaXML = Path.of("game_data/XML/Warframes.xml")
    val salidaXML = Path.of("game_data/XML/Warframes2.xml")
    val datos: List<WarframeXML>
    datos = leerDatosInicialesXML(entradaXML)
    for (dato in datos) {
        println(" - ID: ${dato.warframeId}," +
                " Nombre: ${dato.nombre}," +
                " Vida: ${dato.vida}," +
                " Armadura: ${dato.armadura}," +
                " Energía: ${dato.energia}" +
                " Velocidad de sprint: ${dato.velocidadSprint}" +
                " Pasiva: ${dato.pasiva}")
    }
    escribirDatosXML(salidaXML, datos)
}