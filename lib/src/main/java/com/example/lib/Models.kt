package com.example.lib

import java.util.UUID

// ---- Categorías ----

enum class CategoriaGasto(val label: String) {
    COMIDA("Comida"),
    TRANSPORTE("Transporte"),
    ENTRETENIMIENTO("Entretenimiento"),
    SALUD("Salud"),
    ROPA("Ropa"),
    OTROS("Otros")
}

enum class CategoriaIngreso(val label: String) {
    SUELDO("Sueldo"),
    FREELANCE("Freelance"),
    REGALO("Regalo"),
    INVERSION("Inversión"),
    OTROS("Otros")
}

// ---- Modelos de datos ----

data class Gasto(
    val id: String = UUID.randomUUID().toString(),
    val monto: Double,
    val categoria: CategoriaGasto,
    val fecha: String,
    val descripcion: String = ""
)

data class Ingreso(
    val id: String = UUID.randomUUID().toString(),
    val monto: Double,
    val categoria: CategoriaIngreso,
    val fecha: String,
    val descripcion: String = ""
)

// ---- Funciones auxiliares de fecha ----

// Extrae "MM/yyyy" de una fecha con formato "dd/MM/yyyy"
fun extraerMes(fecha: String): String? =
    if (fecha.length >= 10) fecha.substring(3, 10) else null

val nombresMeses = mapOf(
    "01" to "Enero",   "02" to "Febrero", "03" to "Marzo",
    "04" to "Abril",   "05" to "Mayo",    "06" to "Junio",
    "07" to "Julio",   "08" to "Agosto",  "09" to "Septiembre",
    "10" to "Octubre", "11" to "Noviembre","12" to "Diciembre"
)

// Convierte "04/2026" en "Abril 2026"
fun formatearMes(mesAnio: String): String {
    val numMes = mesAnio.take(2)
    val anio = mesAnio.takeLast(4)
    return "${nombresMeses[numMes] ?: numMes} $anio"
}
