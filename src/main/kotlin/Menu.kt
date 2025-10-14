package org.example

import java.util.Scanner
import WarframeBinario

fun menuPrincipal() {
    val sc = Scanner(System.`in`)
    var opcion: Int?

    while (true) {
        println(
            """
            
            ╔════════════════════════════════════════╗
            ║        MENÚ PRINCIPAL - WARFRAMES      ║
            ╠════════════════════════════════════════╣
            ║ 1. Mostrar todos los registros         ║
            ║ 2. Añadir un nuevo registro            ║
            ║ 3. Modificar un registro (por ID)      ║
            ║ 4. Eliminar un registro (por ID)       ║
            ║ 5. Salir                               ║
            ╚════════════════════════════════════════╝
            """.trimIndent()
        )

        print("Selecciona una opción (1-5): ")
        opcion = sc.nextLine().toIntOrNull()

        when (opcion) {
            1 -> {
                println("\n--- MOSTRAR TODOS LOS REGISTROS ---")
                mostrar()
            }
            2 -> {
                println("\n--- AÑADIR NUEVO REGISTRO ---")
                nuevoReg(sc)
            }
            3 -> {
                println("\n--- MODIFICAR REGISTRO ---")
                print("Introduce el ID del registro a modificar: ")
                val id = sc.nextLine().toIntOrNull()
                if (id != null) {
                    print("Nuevo nombre: ")
                    val nombre = sc.nextLine()
                    print("Nueva pasiva: ")
                    val pasiva = sc.nextLine()
                    modificar(id, nombre, pasiva)
                } else {
                    println("⚠️  ID inválido.")
                }
            }
            4 -> {
                println("\n--- ELIMINAR REGISTRO ---")
                print("Introduce el ID del registro a eliminar: ")
                val id = sc.nextLine().toIntOrNull()
                if (id != null) eliminar(id) else println("⚠️  ID inválido.")
            }
            5 -> {
                println("👋 Saliendo del programa...")
                break
            }
            else -> {
                println("⚠️  Opción inválida. Introduce un número del 1 al 5.")
            }
        }
    }
}

// Función auxiliar para añadir registros nuevos desde consola
fun nuevoReg(sc: Scanner) {
    try {
        print("ID: ")
        val id = sc.nextLine().toIntOrNull() ?: throw Exception("ID inválido")

        print("Nombre: ")
        val nombre = sc.nextLine()

        print("Vida: ")
        val vida = sc.nextLine().toIntOrNull() ?: throw Exception("Valor de vida inválido")

        print("Armadura: ")
        val armadura = sc.nextLine().toIntOrNull() ?: throw Exception("Valor de armadura inválido")

        print("Energía: ")
        val energia = sc.nextLine().toIntOrNull() ?: throw Exception("Valor de energía inválido")

        print("Velocidad de sprint: ")
        val sprint = sc.nextLine().toDoubleOrNull() ?: throw Exception("Valor de velocidad inválido")

        print("Pasiva: ")
        val pasiva = sc.nextLine()

        val nuevo = WarframeBinario(id, nombre, vida, armadura, energia, sprint, pasiva)
        anadir(nuevo)
        println("✅ Registro añadido correctamente.")

    } catch (e: Exception) {
        println("⚠️  Error: ${e.message}")
    }
}
