package com.example.app_gastos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.UUID
import com.example.lib.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditGastoScreen(
    gastoExistente: Gasto?,
    ingresos: List<Ingreso>,
    onGuardar: (Gasto) -> Unit,
    onBack: () -> Unit
) {
    val esEdicion = gastoExistente != null

    var monto by remember { mutableStateOf(gastoExistente?.monto?.toString() ?: "") }
    var categoria by remember { mutableStateOf(gastoExistente?.categoria ?: CategoriaGasto.OTROS) }
    var fecha by remember { mutableStateOf(gastoExistente?.fecha ?: "") }
    var descripcion by remember { mutableStateOf(gastoExistente?.descripcion ?: "") }
    var expandedCategoria by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (esEdicion) "Editar Gasto" else "Agregar Gasto") },
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
            // Monto
            OutlinedTextField(
                value = monto,
                onValueChange = {
                    monto = it
                    errorMensaje = ""
                },
                label = { Text("Monto *") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría
            Box {
                OutlinedTextField(
                    value = categoria.label,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría *") },
                    trailingIcon = {
                        IconButton(onClick = { expandedCategoria = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                DropdownMenu(
                    expanded = expandedCategoria,
                    onDismissRequest = { expandedCategoria = false }
                ) {
                    CategoriaGasto.entries.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.label) },
                            onClick = {
                                categoria = cat
                                expandedCategoria = false
                            }
                        )
                    }
                }
            }

            // Fecha
            OutlinedTextField(
                value = fecha,
                onValueChange = {
                    fecha = it
                    errorMensaje = ""
                },
                label = { Text("Fecha *") },
                placeholder = { Text("dd/MM/yyyy") },
                modifier = Modifier.fillMaxWidth()
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth()
            )

            // Error
            if (errorMensaje.isNotBlank()) {
                Text(errorMensaje, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botón guardar
            Button(
                onClick = {
                    val montoDouble = monto.trim().toDoubleOrNull()
                    val fechaTrim = fecha.trim()
                    val mesFecha = extraerMes(fechaTrim)
                    val tieneIngresoDelMes = mesFecha != null && ingresos.any {
                        extraerMes(it.fecha) == mesFecha
                    }
                    when {
                        montoDouble == null || montoDouble <= 0 ->
                            errorMensaje = "Ingresá un monto válido mayor a 0"
                        fechaTrim.isBlank() ->
                            errorMensaje = "La fecha es obligatoria"
                        mesFecha == null ->
                            errorMensaje = "El formato de fecha debe ser dd/MM/yyyy"
                        !tieneIngresoDelMes ->
                            errorMensaje = "No tenés ingresos en ${formatearMes(mesFecha)}. Primero registrá un ingreso para ese mes."
                        else -> {
                            onGuardar(
                                Gasto(
                                    id = gastoExistente?.id ?: UUID.randomUUID().toString(),
                                    monto = montoDouble,
                                    categoria = categoria,
                                    fecha = fechaTrim,
                                    descripcion = descripcion.trim()
                                )
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (esEdicion) "Guardar cambios" else "Agregar gasto")
            }
        }
    }
}
