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
import com.lazysyntax.nutron.presentation.ui.navigation.composables.BottomNavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.breakfast
import nutron.composeapp.generated.resources.diet
import nutron.composeapp.generated.resources.diet_activity_label
import nutron.composeapp.generated.resources.diet_age_label
import nutron.composeapp.generated.resources.diet_bmr_label
import nutron.composeapp.generated.resources.diet_nutritionist_note_content
import nutron.composeapp.generated.resources.diet_nutritionist_note_title
import nutron.composeapp.generated.resources.diet_objective_label
import nutron.composeapp.generated.resources.diet_planned_meals
import nutron.composeapp.generated.resources.diet_user_profile
import nutron.composeapp.generated.resources.dinner
import nutron.composeapp.generated.resources.lunch
import nutron.composeapp.generated.resources.mid_morning
import nutron.composeapp.generated.resources.post_workout
import nutron.composeapp.generated.resources.profile_height_label
import nutron.composeapp.generated.resources.profile_weight_label
import nutron.composeapp.generated.resources.snack
import nutron.composeapp.generated.resources.title_diet_plan
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietScreen() {
    Scaffold(
        topBar = {
            TopAppBarCommon(title = stringResource(Res.string.title_diet_plan))
        },
        bottomBar = { BottomNavBar() },
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
                    text = stringResource(Res.string.diet_planned_meals),
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
                    text = stringResource(Res.string.diet_user_profile),
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
                InfoColumn(label = stringResource(Res.string.profile_weight_label), value = "89 kg", modifier = Modifier.weight(1f))
                InfoColumn(label = stringResource(Res.string.profile_height_label), value = "193 cm", modifier = Modifier.weight(1f))
                InfoColumn(label = stringResource(Res.string.diet_age_label), value = "34 años", modifier = Modifier.weight(1f))
            }
            
            Row(modifier = Modifier.fillMaxWidth()) {
                InfoColumn(label = stringResource(Res.string.diet_activity_label), value = "Moderado", modifier = Modifier.weight(1f))
                InfoColumn(label = stringResource(Res.string.diet_objective_label), value = "Ganar músculo", modifier = Modifier.weight(1f))
                InfoColumn(label = stringResource(Res.string.diet_bmr_label), value = "2026 kcal", modifier = Modifier.weight(1f))
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
                    text = stringResource(meal.name),
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
            text = stringResource(Res.string.diet_nutritionist_note_title),
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = stringResource(Res.string.diet_nutritionist_note_content),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

data class DemoMeal(
    val time: String,
    val name: org.jetbrains.compose.resources.StringResource,
    val description: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int
)

val demoMeals = listOf(
    DemoMeal("08:00", Res.string.breakfast, "Gachas de avena con proteína en polvo y arándanos", 450, 30, 50, 10),
    DemoMeal("11:00", Res.string.mid_morning, "Yogur griego con nueces y miel", 250, 20, 15, 12),
    DemoMeal("14:00", Res.string.lunch, "Pechuga de pollo a la plancha con arroz integral y brócoli", 600, 45, 60, 15),
    DemoMeal("17:00", Res.string.snack, "Batido de proteínas y una manzana", 200, 25, 20, 2),
    DemoMeal("20:00", Res.string.dinner, "Salmón al horno con boniato y espárragos", 550, 40, 40, 20),
    DemoMeal("23:00", Res.string.post_workout, "Queso cottage con almendras", 200, 25, 5, 8)
)
