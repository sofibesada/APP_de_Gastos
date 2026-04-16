package com.example.app_gastos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app_gastos.ui.theme.APP_GastosTheme
import com.example.lib.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APP_GastosTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val gastos = remember { mutableStateListOf<Gasto>() }
    val ingresos = remember { mutableStateListOf<Ingreso>() }

    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onNavigateGastos = { navController.navigate("gastos") },
                onNavigateIngresos = { navController.navigate("ingresos") },
                onNavigateEstadisticas = { navController.navigate("estadisticas") },
                onNavigateAcercaDe = { navController.navigate("acercaDe") }
            )
        }

        composable("acercaDe") {
            AcercaDeScreen(onBack = { navController.popBackStack() })
        }

        composable("gastos") {
            GastosScreen(
                gastos = gastos,
                onAgregar = { navController.navigate("addGasto") },
                onEditar = { id -> navController.navigate("editGasto/$id") },
                onEliminar = { id ->
                    val index = gastos.indexOfFirst { it.id == id }
                    if (index != -1) gastos.removeAt(index)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addGasto") {
            AddEditGastoScreen(
                gastoExistente = null,
                onGuardar = { gasto ->
                    gastos.add(gasto)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("editGasto/{gastoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("gastoId") ?: ""
            val gasto = gastos.find { it.id == id }
            AddEditGastoScreen(
                gastoExistente = gasto,
                onGuardar = { updatedGasto ->
                    val index = gastos.indexOfFirst { it.id == updatedGasto.id }
                    if (index != -1) gastos[index] = updatedGasto
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("ingresos") {
            IngresosScreen(
                ingresos = ingresos,
                onAgregar = { navController.navigate("addIngreso") },
                onEditar = { id -> navController.navigate("editIngreso/$id") },
                onEliminar = { id ->
                    val index = ingresos.indexOfFirst { it.id == id }
                    if (index != -1) ingresos.removeAt(index)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addIngreso") {
            AddEditIngresoScreen(
                ingresoExistente = null,
                onGuardar = { ingreso ->
                    ingresos.add(ingreso)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("editIngreso/{ingresoId}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("ingresoId") ?: ""
            val ingreso = ingresos.find { it.id == id }
            AddEditIngresoScreen(
                ingresoExistente = ingreso,
                onGuardar = { updatedIngreso ->
                    val index = ingresos.indexOfFirst { it.id == updatedIngreso.id }
                    if (index != -1) ingresos[index] = updatedIngreso
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("estadisticas") {
            EstadisticasScreen(
                gastos = gastos,
                ingresos = ingresos,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
