package com.example.app_gastos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    gastos: SnapshotStateList<Gasto>,
    ingresos: SnapshotStateList<Ingreso>,
    onBack: () -> Unit
) {
    var tipoFiltro by remember { mutableStateOf("Mes") }
    var filtroValor by remember { mutableStateOf("") }

    val gastosFiltrados = if (filtroValor.isBlank()) {
        gastos.toList()
    } else {
        gastos.filter { it.fecha.contains(filtroValor) }
    }

    val ingresosFiltrados = if (filtroValor.isBlank()) {
        ingresos.toList()
    } else {
        ingresos.filter { it.fecha.contains(filtroValor) }
    }

    val totalGastos = gastosFiltrados.sumOf { it.monto }
    val totalIngresos = ingresosFiltrados.sumOf { it.monto }
    val balance = totalIngresos - totalGastos

    val gastosPorCategoria = CategoriaGasto.entries.map { cat ->
        cat to gastosFiltrados.filter { it.categoria == cat }.sumOf { it.monto }
    }.filter { (_, total) -> total > 0 }

    val ingresosPorCategoria = CategoriaIngreso.entries.map { cat ->
        cat to ingresosFiltrados.filter { it.categoria == cat }.sumOf { it.monto }
    }.filter { (_, total) -> total > 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selector de tipo de filtro
            Text("Filtrar por:", fontWeight = FontWeight.Medium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Día", "Mes", "Año").forEach { tipo ->
                    if (tipoFiltro == tipo) {
                        Button(onClick = { tipoFiltro = tipo; filtroValor = "" }) {
                            Text(tipo)
                        }
                    } else {
                        OutlinedButton(onClick = { tipoFiltro = tipo; filtroValor = "" }) {
                            Text(tipo)
                        }
                    }
                }
            }

            // Campo de filtro
            OutlinedTextField(
                value = filtroValor,
                onValueChange = { filtroValor = it },
                label = { Text("Filtrar por $tipoFiltro") },
                placeholder = {
                    Text(
                        when (tipoFiltro) {
                            "Día" -> "ej: 12/04/2026"
                            "Mes" -> "ej: 04/2026"
                            else -> "ej: 2026"
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            if (filtroValor.isBlank()) {
                Text(
                    "Mostrando todos los registros",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Resumen general
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Resumen", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Ingresos:")
                        Text(
                            "+ $ %.2f".format(totalIngresos),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Gastos:")
                        Text(
                            "- $ %.2f".format(totalGastos),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Balance:", fontWeight = FontWeight.Bold)
                        Text(
                            "$ %.2f".format(balance),
                            fontWeight = FontWeight.Bold,
                            color = if (balance >= 0) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // Gastos por categoría
            if (gastosPorCategoria.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Gastos por Categoría", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        HorizontalDivider()
                        gastosPorCategoria.forEach { (cat, total) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(cat.label)
                                Text("$ %.2f".format(total))
                            }
                        }
                    }
                }
            }

            // Ingresos por categoría
            if (ingresosPorCategoria.isNotEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Ingresos por Categoría", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        HorizontalDivider()
                        ingresosPorCategoria.forEach { (cat, total) ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(cat.label)
                                Text("$ %.2f".format(total))
                            }
                        }
                    }
                }
            }

            // Balance por mes
            val mesesUnicos = (gastos.mapNotNull { extraerMes(it.fecha) } +
                               ingresos.mapNotNull { extraerMes(it.fecha) })
                .distinct()
                .sortedWith(compareBy({ it.takeLast(4) }, { it.take(2) }))

            if (mesesUnicos.isNotEmpty()) {
                Text("Balance por Mes", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                mesesUnicos.forEach { mes ->
                    val ingresosDelMes = ingresos.filter { extraerMes(it.fecha) == mes }.sumOf { it.monto }
                    val gastosDelMes = gastos.filter { extraerMes(it.fecha) == mes }.sumOf { it.monto }
                    val sobrante = ingresosDelMes - gastosDelMes

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (sobrante >= 0)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                formatearMes(mes),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ingresos:")
                                Text(
                                    "+ $ %.2f".format(ingresosDelMes),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gastos:")
                                Text(
                                    "- $ %.2f".format(gastosDelMes),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                            HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    if (sobrante >= 0) "Te sobró:" else "Te faltó:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    "$ %.2f".format(sobrante),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = if (sobrante >= 0)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
