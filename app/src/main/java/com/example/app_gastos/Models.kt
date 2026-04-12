package com.example.app_gastos

import java.util.UUID

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
