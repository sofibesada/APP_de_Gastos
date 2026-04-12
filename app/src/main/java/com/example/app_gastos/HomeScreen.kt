package com.example.app_gastos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(
    onNavigateGastos: () -> Unit,
    onNavigateIngresos: () -> Unit,
    onNavigateEstadisticas: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Control de Gastos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Administrá tus finanzas personales",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(56.dp))

            Button(
                onClick = onNavigateGastos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Gastos", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateIngresos,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresos", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onNavigateEstadisticas,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Estadísticas", fontSize = 18.sp)
            }
        }
    }
}
