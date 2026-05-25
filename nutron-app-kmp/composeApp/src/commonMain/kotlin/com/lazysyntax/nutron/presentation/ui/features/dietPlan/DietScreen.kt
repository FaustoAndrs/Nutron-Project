package com.lazysyntax.nutron.presentation.ui.features.dietPlan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.diet
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen() {
    Scaffold(
        topBar = {
            TopAppBarCommon(title = "Plan de Dieta")
        },
        bottomBar = { NavBar() },
        containerColor = MaterialTheme.colorScheme.background // iOS background color (SystemGroupedBackground)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                UserProfileHeader()
            }

            item {
                Text(
                    text = "Comidas Planificadas",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(demoMeals) { meal ->
                MealItem(meal)
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
                NutritionistNote()
            }
        }
    }
}

@Composable
fun UserProfileHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perfil del Usuario",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.diet),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColumn(label = "Peso", value = "89 kg", modifier = Modifier.weight(1f))
                InfoColumn(label = "Altura", value = "193 cm", modifier = Modifier.weight(1f))
                InfoColumn(label = "Edad", value = "34 años", modifier = Modifier.weight(1f))
            }
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColumn(label = "Actividad", value = "Moderado", modifier = Modifier.weight(1f))
                InfoColumn(label = "Objetivo", value = "Ganar músculo", modifier = Modifier.weight(1f))
                InfoColumn(label = "BMR", value = "2026 kcal", modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
fun MealItem(meal: DemoMeal) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = meal.time,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = meal.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${meal.calories} kcal",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "P: ${meal.protein}g | C: ${meal.carbs}g | G: ${meal.fats}g",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun NutritionistNote() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Nota del Nutricionista",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Este plan está diseñado para maximizar la síntesis proteica y proporcionar energía suficiente para tus entrenamientos moderados. Asegúrate de mantenerte hidratado.",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

data class DemoMeal(
    val time: String,
    val name: String,
    val description: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)

val demoMeals = listOf(
    DemoMeal("08:00", "Desayuno", "Gachas de avena con proteína en polvo y arándanos", 450, 30, 50, 10),
    DemoMeal("11:00", "Media Mañana", "Yogur griego con nueces y miel", 250, 20, 15, 12),
    DemoMeal("14:00", "Almuerzo", "Pechuga de pollo a la plancha con arroz integral y brócoli", 600, 45, 60, 15),
    DemoMeal("17:00", "Merienda", "Batido de proteínas y una manzana", 200, 25, 20, 2),
    DemoMeal("20:00", "Cena", "Salmón al horno con boniato y espárragos", 550, 40, 40, 20),
    DemoMeal("23:00", "Post-entreno/Resena", "Queso cottage con almendras", 200, 25, 5, 8)
)
