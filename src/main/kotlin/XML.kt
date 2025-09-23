import java.nio.file.Path
import java.io.File
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

data class Planta(
    @JacksonXmlProperty(localName = "id_planta")
    val id_planta: Int,
    @JacksonXmlProperty(localName = "nombre_comun")
    val nombre_comun: String,
    @JacksonXmlProperty(localName = "nombre_cientifico")
    val nombre_cientifico: String,
    @JacksonXmlProperty(localName = "frecuencia_riego")
    val frecuencia_riego: Int,
    @JacksonXmlProperty(localName = "altura_maxima")
    val altura_maxima: Double
)

@JacksonXmlRootElement(localName = "plantas")

data class Plantas(
    @JacksonXmlElementWrapper(useWrapping = false)
@JacksonXmlProperty(localName = "planta")
val listaPlantas: List<Planta> = emptyList()
)

fun leerDatosInicialesXML(ruta: Path): List<Planta> {

    val fichero: File = ruta.toFile()

    val xmlMapper = XmlMapper().registerKotlinModule()

    val plantasWrapper: Plantas = xmlMapper.readValue(fichero)
    return plantasWrapper.listaPlantas
}
fun escribirDatosXML(ruta: Path,plantas: List<Planta>) {
    try {
        val fichero: File = ruta.toFile()

        val contenedorXml = Plantas(plantas)

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
    val entradaXML = Path.of("documentos/mis_plantas.xml")
    val salidaXML = Path.of("documentos/mis_plantas2.xml")
    val datos: List<Planta>
    datos = leerDatosInicialesXML(entradaXML)
    for (dato in datos) {
        println(" - ID: ${dato.id_planta}, Nombre común: ${dato.nombre_comun}, " +
                "Nombre científico: ${dato.nombre_cientifico}," +
                " Frecuencia de riego: ${dato.frecuencia_riego} días," +
                " Altura: ${dato.altura_maxima} metros")
    }
    escribirDatosXML(salidaXML, datos)
}