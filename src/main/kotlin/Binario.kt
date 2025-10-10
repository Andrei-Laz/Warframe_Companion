package org.example

import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.*
import kotlin.io.path.exists
import kotlin.io.path.name

import WarframeBinario
import leerDatosInicialesJSON

// Tamaños fijos
private const val NAME_SIZE = 30
private const val PASSIVE_SIZE = 50
private const val RECORD_SIZE = 4 + 4 + 4 + 4 + 8 + NAME_SIZE + PASSIVE_SIZE // 104 bytes

private val rutaBinario = Path.of("game_data/datos_fin/datos_finales.bin")

// Rellena o recorta cadenas
private fun String.toFixedLength(size: Int): String {
    return if (this.length > size) this.substring(0, size)
    else this.padEnd(size, ' ')
}

// Limpia al leer
private fun String.clean(): String = this.trimEnd()

// 2️⃣ Crear o vaciar el fichero binario
fun vaciarCrearFichero() {
    val carpeta = rutaBinario.parent
    if (!Files.exists(carpeta)) Files.createDirectories(carpeta)

    Files.newByteChannel(rutaBinario,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE
    ).use {
        println("Fichero binario vacío o recién creado: ${rutaBinario.name}")
    }
}

// 3️⃣ Añadir un nuevo registro
fun anadir(w: WarframeBinario) {
    val buffer = ByteBuffer.allocate(RECORD_SIZE)
    buffer.putInt(w.warframeId)
    buffer.putInt(w.health)
    buffer.putInt(w.armor)
    buffer.putInt(w.energy)
    buffer.putDouble(w.sprintSpeed)
    buffer.put(w.name.toFixedLength(NAME_SIZE).toByteArray(Charsets.UTF_8))
    buffer.put(w.passive.toFixedLength(PASSIVE_SIZE).toByteArray(Charsets.UTF_8))
    buffer.flip()

    FileChannel.open(rutaBinario, StandardOpenOption.CREATE, StandardOpenOption.APPEND).use { ch ->
        ch.write(buffer)
    }
}


fun importarDesdeJSON() {
    val rutaJSON = Path.of("game_data/JSON/Warframes.json")
    val datos = leerDatosInicialesJSON(rutaJSON)
    for (d in datos) {
        val w = WarframeBinario(
            warframeId = d.warframeId,
            name = d.name,
            health = d.health,
            armor = d.armor,
            energy = d.energy,
            sprintSpeed = d.sprintSpeed,
            passive = d.passive
        )
        anadir(w)
    }
    println("Importados ${datos.size} registros al fichero binario.")
}

// 5️⃣ Mostrar todos los registros
fun mostrar() {
    if (!rutaBinario.exists()) {
        println("No existe el fichero binario.")
        return
    }

    FileChannel.open(rutaBinario, StandardOpenOption.READ).use { ch ->
        val buffer = ByteBuffer.allocate(RECORD_SIZE)
        var index = 0

        println("\nContenido del fichero binario:\n")

        while (ch.read(buffer) > 0) {
            buffer.flip()

            val id = buffer.int
            val health = buffer.int
            val armor = buffer.int
            val energy = buffer.int
            val sprint = buffer.double

            val nameBytes = ByteArray(NAME_SIZE)
            buffer.get(nameBytes)
            val passiveBytes = ByteArray(PASSIVE_SIZE)
            buffer.get(passiveBytes)

            val name = String(nameBytes, Charsets.UTF_8).clean()
            val passive = String(passiveBytes, Charsets.UTF_8).clean()

            println("Registro #${index++} -> ID: $id, Nombre: $name, Vida: $health, Armadura: $armor, Energía: $energy, Sprint: $sprint, Pasiva: $passive")
            buffer.clear()
        }
    }
}

// 7️⃣ Modificar registro por ID
fun modificar(id: Int, nuevoNombre: String, nuevaPasiva: String) {
    FileChannel.open(rutaBinario, StandardOpenOption.READ, StandardOpenOption.WRITE).use { ch ->
        val buffer = ByteBuffer.allocate(RECORD_SIZE)
        var pos = 0L

        while (ch.read(buffer) > 0) {
            buffer.flip()
            val currentId = buffer.int
            if (currentId == id) {
                // Nos posicionamos al inicio del registro
                ch.position(pos)

                // Sobrescribimos campos: saltamos 4+4+4+4+8 bytes hasta name
                ch.position(pos + 4 + 4 + 4 + 4 + 8)
                val updateBuffer = ByteBuffer.allocate(NAME_SIZE + PASSIVE_SIZE)
                updateBuffer.put(nuevoNombre.toFixedLength(NAME_SIZE).toByteArray(Charsets.UTF_8))
                updateBuffer.put(nuevaPasiva.toFixedLength(PASSIVE_SIZE).toByteArray(Charsets.UTF_8))
                updateBuffer.flip()
                ch.write(updateBuffer)
                println("Registro con ID=$id modificado.")
                return
            }
            pos += RECORD_SIZE
            buffer.clear()
        }
        println("No se encontró el registro con ID=$id.")
    }
}

// 8️⃣ Eliminar registro
fun eliminar(id: Int) {
    val tempPath = Path.of("game_data/datos_fin/temp.bin")

    FileChannel.open(rutaBinario, StandardOpenOption.READ).use { src ->
        FileChannel.open(tempPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING).use { dst ->
            val buffer = ByteBuffer.allocate(RECORD_SIZE)
            while (src.read(buffer) > 0) {
                buffer.flip()
                val currentId = buffer.int
                buffer.rewind()
                if (currentId != id) dst.write(buffer)
                buffer.clear()
            }
        }
    }
    Files.deleteIfExists(rutaBinario)
    Files.move(tempPath, rutaBinario, StandardCopyOption.REPLACE_EXISTING)
    println("Registro con ID=$id eliminado.")
}
