package com.lazysyntax.nutron.data.util

import com.lazysyntax.nutron.data.local.meal.toEntity
import com.lazysyntax.nutron.data.local.meal.toSnapshotEntities
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Nutriments
import com.lazysyntax.nutron.domain.models.round
import com.lazysyntax.nutron.domain.repository.FoodRepository
import com.lazysyntax.nutron.domain.repository.MealRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TestDataGenerator(
    private val foodRepository: FoodRepository,
    private val mealRepository: MealRepository,
    private val sessionManager: SessionManager
) {

    private val sampleFoods = listOf(
        Food(barcode = "101", name = "Pechuga de Pollo", nutriments = Nutriments(calories = 165.0, proteins = 31.0, carbs = 0.0, fat = 3.6)),
        Food(barcode = "102", name = "Arroz Integral", nutriments = Nutriments(calories = 111.0, proteins = 2.6, carbs = 23.0, fat = 0.9)),
        Food(barcode = "103", name = "Huevos Grandes", nutriments = Nutriments(calories = 155.0, proteins = 13.0, carbs = 1.1, fat = 11.0)),
        Food(barcode = "104", name = "Avena", nutriments = Nutriments(calories = 389.0, proteins = 16.9, carbs = 66.3, fat = 6.9)),
        Food(barcode = "105", name = "Aguacate", nutriments = Nutriments(calories = 160.0, proteins = 2.0, carbs = 8.5, fat = 14.7)),
        Food(barcode = "106", name = "Plátano", nutriments = Nutriments(calories = 89.0, proteins = 1.1, carbs = 22.8, fat = 0.3)),
        Food(barcode = "107", name = "Atún en lata", nutriments = Nutriments(calories = 116.0, proteins = 26.0, carbs = 0.0, fat = 1.0)),
        Food(barcode = "108", name = "Leche Semidesnatada", nutriments = Nutriments(calories = 47.0, proteins = 3.4, carbs = 4.8, fat = 1.6, quantityUnit = "ml")),
        Food(barcode = "109", name = "Pan Integral", nutriments = Nutriments(calories = 247.0, proteins = 13.0, carbs = 41.0, fat = 3.4)),
        Food(barcode = "110", name = "Manzana", nutriments = Nutriments(calories = 52.0, proteins = 0.3, carbs = 13.8, fat = 0.2)),
        Food(barcode = "111", name = "Salmón Fresco", nutriments = Nutriments(calories = 208.0, proteins = 20.0, carbs = 0.0, fat = 13.0)),
        Food(barcode = "112", name = "Espinacas Frescas", nutriments = Nutriments(calories = 23.0, proteins = 2.9, carbs = 3.6, fat = 0.4)),
        Food(barcode = "113", name = "Yogur Griego Natural", nutriments = Nutriments(calories = 59.0, proteins = 10.0, carbs = 3.6, fat = 0.4)),
        Food(barcode = "114", name = "Almendras", nutriments = Nutriments(calories = 579.0, proteins = 21.0, carbs = 22.0, fat = 50.0)),
        Food(barcode = "115", name = "Aceite de Oliva Extra Virgen", nutriments = Nutriments(calories = 884.0, proteins = 0.0, carbs = 0.0, fat = 100.0, quantityUnit = "ml")),
        Food(barcode = "116", name = "Lentejas Cocidas", nutriments = Nutriments(calories = 116.0, proteins = 9.0, carbs = 20.0, fat = 0.4)),
        Food(barcode = "117", name = "Queso Fresco Batido 0%", nutriments = Nutriments(calories = 46.0, proteins = 8.0, carbs = 3.5, fat = 0.1)),
        Food(barcode = "118", name = "Chocolate Negro 85%", nutriments = Nutriments(calories = 576.0, proteins = 9.0, carbs = 19.0, fat = 46.0)),
        Food(barcode = "119", name = "Brócoli", nutriments = Nutriments(calories = 34.0, proteins = 2.8, carbs = 7.0, fat = 0.4)),
        Food(barcode = "120", name = "Garbanzos en conserva", nutriments = Nutriments(calories = 139.0, proteins = 7.0, carbs = 23.0, fat = 2.5)),
        Food(barcode = "121", name = "Patata Cocida", nutriments = Nutriments(calories = 87.0, proteins = 1.9, carbs = 20.1, fat = 0.1)),
        Food(barcode = "122", name = "Tofu Firme", nutriments = Nutriments(calories = 144.0, proteins = 17.0, carbs = 2.8, fat = 8.0)),
        Food(barcode = "123", name = "Crema de Cacahuete 100%", nutriments = Nutriments(calories = 588.0, proteins = 25.0, carbs = 20.0, fat = 50.0)),
        Food(barcode = "124", name = "Naranjas", nutriments = Nutriments(calories = 47.0, proteins = 0.9, carbs = 11.8, fat = 0.1)),
        Food(barcode = "125", name = "Carne Picada de Ternera (Magra)", nutriments = Nutriments(calories = 137.0, proteins = 21.4, carbs = 0.0, fat = 5.7)),
        Food(barcode = "126", name = "Quinoa Cocida", nutriments = Nutriments(calories = 120.0, proteins = 4.4, carbs = 21.3, fat = 1.9)),
        Food(barcode = "127", name = "Queso Mozzarella", nutriments = Nutriments(calories = 280.0, proteins = 28.0, carbs = 3.1, fat = 17.0)),
        Food(barcode = "128", name = "Arándanos", nutriments = Nutriments(calories = 57.0, proteins = 0.7, carbs = 14.5, fat = 0.3)),
        Food(barcode = "129", name = "Café Solo (Bebida)", nutriments = Nutriments(calories = 1.0, proteins = 0.1, carbs = 0.0, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "130", name = "Bebida de Almedras sin azúcar", nutriments = Nutriments(calories = 13.0, proteins = 0.4, carbs = 0.2, fat = 1.1, quantityUnit = "ml")),
    // --- PESCADOS Y MARISCOS (131 - 140) ---
        Food(barcode = "131", name = "Bacalao Fresco", nutriments = Nutriments(calories = 82.0, proteins = 17.8, carbs = 0.0, fat = 0.7)),
        Food(barcode = "132", name = "Gambas Peladas", nutriments = Nutriments(calories = 85.0, proteins = 20.1, carbs = 0.0, fat = 0.5)),
        Food(barcode = "133", name = "Merluza", nutriments = Nutriments(calories = 89.0, proteins = 15.9, carbs = 0.0, fat = 2.8)),
        Food(barcode = "134", name = "Sardinas en Tomate", nutriments = Nutriments(calories = 185.0, proteins = 16.0, carbs = 2.0, fat = 12.0)),
        Food(barcode = "135", name = "Pulpo Cocido", nutriments = Nutriments(calories = 86.0, proteins = 18.0, carbs = 0.0, fat = 1.0)),
        Food(barcode = "136", name = "Trucha", nutriments = Nutriments(calories = 141.0, proteins = 20.0, carbs = 0.0, fat = 6.2)),
        Food(barcode = "137", name = "Emperador / Pez Espada", nutriments = Nutriments(calories = 144.0, proteins = 20.0, carbs = 0.0, fat = 6.7)),
        Food(barcode = "138", name = "Calamares", nutriments = Nutriments(calories = 92.0, proteins = 15.6, carbs = 3.1, fat = 1.4)),
        Food(barcode = "139", name = "Mejillones en Escabeche", nutriments = Nutriments(calories = 162.0, proteins = 14.0, carbs = 4.5, fat = 10.0)),
        Food(barcode = "140", name = "Boquerones en Vinagre", nutriments = Nutriments(calories = 131.0, proteins = 17.0, carbs = 0.5, fat = 7.0)),

    // --- CARNES Y AVES (141 - 150) ---
        Food(barcode = "141", name = "Contramuslo de Pollo", nutriments = Nutriments(calories = 177.0, proteins = 18.2, carbs = 0.0, fat = 11.4)),
        Food(barcode = "142", name = "Solomillo de Cerdo", nutriments = Nutriments(calories = 143.0, proteins = 22.0, carbs = 0.0, fat = 6.0)),
        Food(barcode = "143", name = "Pechuga de Pavo", nutriments = Nutriments(calories = 105.0, proteins = 24.0, carbs = 1.0, fat = 0.5)),
        Food(barcode = "144", name = "Chuleta de Cordero", nutriments = Nutriments(calories = 232.0, proteins = 18.0, carbs = 0.0, fat = 18.0)),
        Food(barcode = "145", name = "Jamón Serrano", nutriments = Nutriments(calories = 242.0, proteins = 30.0, carbs = 0.5, fat = 13.0)),
        Food(barcode = "146", name = "Jamón Cocido Extra", nutriments = Nutriments(calories = 105.0, proteins = 19.0, carbs = 1.0, fat = 2.7)),
        Food(barcode = "147", name = "Cecina de León", nutriments = Nutriments(calories = 250.0, proteins = 39.0, carbs = 1.0, fat = 9.5)),
        Food(barcode = "148", name = "Chorizo Ibérico", nutriments = Nutriments(calories = 450.0, proteins = 22.0, carbs = 1.5, fat = 40.0)),
        Food(barcode = "149", name = "Carne de Conejo", nutriments = Nutriments(calories = 133.0, proteins = 22.0, carbs = 0.0, fat = 5.0)),
        Food(barcode = "150", name = "Hamburguesa de Vacuno", nutriments = Nutriments(calories = 230.0, proteins = 17.5, carbs = 0.0, fat = 18.0)),

    // --- LÁCTEOS Y DERIVADOS (151 - 160) ---
        Food(barcode = "151", name = "Leche Entera", nutriments = Nutriments(calories = 62.0, proteins = 3.2, carbs = 4.7, fat = 3.5, quantityUnit = "ml")),
        Food(barcode = "152", name = "Leche Desnatada", nutriments = Nutriments(calories = 34.0, proteins = 3.4, carbs = 4.9, fat = 0.1, quantityUnit = "ml")),
        Food(barcode = "153", name = "Queso Parmesano", nutriments = Nutriments(calories = 431.0, proteins = 38.0, carbs = 4.1, fat = 29.0)),
        Food(barcode = "154", name = "Queso Cheddar", nutriments = Nutriments(calories = 403.0, proteins = 25.0, carbs = 1.3, fat = 33.0)),
        Food(barcode = "155", name = "Queso Feta", nutriments = Nutriments(calories = 264.0, proteins = 14.0, carbs = 4.1, fat = 21.0)),
        Food(barcode = "156", name = "Mantequilla", nutriments = Nutriments(calories = 717.0, proteins = 0.9, carbs = 0.1, fat = 81.0)),
        Food(barcode = "157", name = "Kéfir Natural", nutriments = Nutriments(calories = 64.0, proteins = 3.5, carbs = 4.8, fat = 3.5, quantityUnit = "ml")),
        Food(barcode = "158", name = "Nata para Cocinar 15%", nutriments = Nutriments(calories = 161.0, proteins = 2.7, carbs = 4.0, fat = 15.0, quantityUnit = "ml")),
        Food(barcode = "159", name = "Yogur Proteico Fresa", nutriments = Nutriments(calories = 60.0, proteins = 10.0, carbs = 4.5, fat = 0.2)),
        Food(barcode = "160", name = "Queso de Cabra Semicurado", nutriments = Nutriments(calories = 390.0, proteins = 24.0, carbs = 1.0, fat = 32.0)),

    // --- VERDURAS Y HORTALIZAS (161 - 175) ---
        Food(barcode = "161", name = "Zanahoria", nutriments = Nutriments(calories = 41.0, proteins = 0.9, carbs = 9.6, fat = 0.2)),
        Food(barcode = "162", name = "Tomate", nutriments = Nutriments(calories = 18.0, proteins = 0.9, carbs = 3.9, fat = 0.2)),
        Food(barcode = "163", name = "Lechuga Iceberg", nutriments = Nutriments(calories = 14.0, proteins = 0.9, carbs = 3.0, fat = 0.1)),
        Food(barcode = "164", name = "Pepino", nutriments = Nutriments(calories = 15.0, proteins = 0.7, carbs = 3.6, fat = 0.1)),
        Food(barcode = "165", name = "Pimiento Rojo", nutriments = Nutriments(calories = 31.0, proteins = 1.0, carbs = 6.0, fat = 0.3)),
        Food(barcode = "166", name = "Calabacín", nutriments = Nutriments(calories = 17.0, proteins = 1.2, carbs = 3.1, fat = 0.3)),
        Food(barcode = "167", name = "Berenjena", nutriments = Nutriments(calories = 25.0, proteins = 1.0, carbs = 6.0, fat = 0.2)),
        Food(barcode = "168", name = "Cebolla", nutriments = Nutriments(calories = 40.0, proteins = 1.1, carbs = 9.3, fat = 0.1)),
        Food(barcode = "169", name = "Ajo", nutriments = Nutriments(calories = 149.0, proteins = 6.4, carbs = 33.0, fat = 0.5)),
        Food(barcode = "170", name = "Champiñones", nutriments = Nutriments(calories = 22.0, proteins = 3.1, carbs = 3.3, fat = 0.3)),
        Food(barcode = "171", name = "Espárragos Verdes", nutriments = Nutriments(calories = 20.0, proteins = 2.2, carbs = 3.9, fat = 0.1)),
        Food(barcode = "172", name = "Coliflor", nutriments = Nutriments(calories = 25.0, proteins = 1.9, carbs = 5.0, fat = 0.3)),
        Food(barcode = "173", name = "Judías Verdes", nutriments = Nutriments(calories = 31.0, proteins = 1.8, carbs = 7.0, fat = 0.2)),
        Food(barcode = "174", name = "Calabaza", nutriments = Nutriments(calories = 26.0, proteins = 1.0, carbs = 6.5, fat = 0.1)),
        Food(barcode = "175", name = "Puerro", nutriments = Nutriments(calories = 61.0, proteins = 1.5, carbs = 14.0, fat = 0.3)),

    // --- FRUTAS (176 - 190) ---
        Food(barcode = "176", name = "Fresas", nutriments = Nutriments(calories = 32.0, proteins = 0.7, carbs = 7.7, fat = 0.3)),
        Food(barcode = "177", name = "Uvas Verdes", nutriments = Nutriments(calories = 69.0, proteins = 0.7, carbs = 18.1, fat = 0.2)),
        Food(barcode = "178", name = "Pera", nutriments = Nutriments(calories = 57.0, proteins = 0.4, carbs = 15.0, fat = 0.1)),
        Food(barcode = "179", name = "Melocotón", nutriments = Nutriments(calories = 39.0, proteins = 0.9, carbs = 9.5, fat = 0.3)),
        Food(barcode = "180", name = "Piña", nutriments = Nutriments(calories = 50.0, proteins = 0.5, carbs = 13.0, fat = 0.1)),
        Food(barcode = "181", name = "Mango", nutriments = Nutriments(calories = 60.0, proteins = 0.8, carbs = 15.0, fat = 0.4)),
        Food(barcode = "182", name = "Sandía", nutriments = Nutriments(calories = 30.0, proteins = 0.6, carbs = 7.6, fat = 0.2)),
        Food(barcode = "183", name = "Melón", nutriments = Nutriments(calories = 34.0, proteins = 0.8, carbs = 8.0, fat = 0.2)),
        Food(barcode = "184", name = "Kiwi", nutriments = Nutriments(calories = 61.0, proteins = 1.1, carbs = 15.0, fat = 0.5)),
        Food(barcode = "185", name = "Limón", nutriments = Nutriments(calories = 29.0, proteins = 1.1, carbs = 9.3, fat = 0.3)),
        Food(barcode = "186", name = "Mandarina", nutriments = Nutriments(calories = 53.0, proteins = 0.8, carbs = 13.3, fat = 0.3)),
        Food(barcode = "187", name = "Cerezas", nutriments = Nutriments(calories = 50.0, proteins = 1.0, carbs = 12.0, fat = 0.3)),
        Food(barcode = "188", name = "Ciruela", nutriments = Nutriments(calories = 46.0, proteins = 0.7, carbs = 11.4, fat = 0.3)),
        Food(barcode = "189", name = "Frambuesas", nutriments = Nutriments(calories = 52.0, proteins = 1.2, carbs = 12.0, fat = 0.7)),
        Food(barcode = "190", name = "Higos", nutriments = Nutriments(calories = 74.0, proteins = 0.8, carbs = 19.0, fat = 0.3)),

    // --- CEREALES, TUBÉRCULOS Y LEGUMBRES (191 - 205) ---
        Food(barcode = "191", name = "Arroz Blanco", nutriments = Nutriments(calories = 130.0, proteins = 2.7, carbs = 28.0, fat = 0.3)),
        Food(barcode = "192", name = "Pasta de Trigo (Seca)", nutriments = Nutriments(calories = 354.0, proteins = 12.0, carbs = 73.0, fat = 1.5)),
        Food(barcode = "193", name = "Pasta Integral (Seca)", nutriments = Nutriments(calories = 332.0, proteins = 13.0, carbs = 65.0, fat = 2.5)),
        Food(barcode = "194", name = "Boniato / Camote", nutriments = Nutriments(calories = 86.0, proteins = 1.6, carbs = 20.1, fat = 0.1)),
        Food(barcode = "195", name = "Alubias Blancas cocidas", nutriments = Nutriments(calories = 139.0, proteins = 9.7, carbs = 25.0, fat = 0.4)),
        Food(barcode = "196", name = "Alubias Negras cocidas", nutriments = Nutriments(calories = 132.0, proteins = 8.9, carbs = 23.7, fat = 0.5)),
        Food(barcode = "197", name = "Gofio de Trigo", nutriments = Nutriments(calories = 365.0, proteins = 11.0, carbs = 74.0, fat = 2.5)),
        Food(barcode = "198", name = "Pan de Centeno", nutriments = Nutriments(calories = 259.0, proteins = 8.5, carbs = 48.0, fat = 3.3)),
        Food(barcode = "199", name = "Tortitas de Arroz", nutriments = Nutriments(calories = 387.0, proteins = 8.0, carbs = 81.0, fat = 2.8)),
        Food(barcode = "200", name = "Cuscús Cocido", nutriments = Nutriments(calories = 112.0, proteins = 3.8, carbs = 23.0, fat = 0.2)),
        Food(barcode = "201", name = "Harina de Avena", nutriments = Nutriments(calories = 380.0, proteins = 14.0, carbs = 65.0, fat = 7.0)),
        Food(barcode = "202", name = "Salvado de Trigo", nutriments = Nutriments(calories = 216.0, proteins = 15.0, carbs = 65.0, fat = 4.3)),
        Food(barcode = "203", name = "Gisantes Verdes (Cocidos)", nutriments = Nutriments(calories = 81.0, proteins = 5.4, carbs = 14.5, fat = 0.4)),
        Food(barcode = "204", name = "Maíz en grano dulce", nutriments = Nutriments(calories = 86.0, proteins = 3.2, carbs = 19.0, fat = 1.2)),
        Food(barcode = "205", name = "Pan de Molde Blanco", nutriments = Nutriments(calories = 265.0, proteins = 9.0, carbs = 49.0, fat = 3.2)),

    // --- FRUTOS SECOS Y SEMILLAS (206 - 215) ---
        Food(barcode = "206", name = "Nueces", nutriments = Nutriments(calories = 654.0, proteins = 15.0, carbs = 14.0, fat = 65.0)),
        Food(barcode = "207", name = "Pistachos", nutriments = Nutriments(calories = 562.0, proteins = 20.0, carbs = 27.0, fat = 45.0)),
        Food(barcode = "208", name = "Avellanas", nutriments = Nutriments(calories = 628.0, proteins = 15.0, carbs = 17.0, fat = 61.0)),
        Food(barcode = "209", name = "Anacardos", nutriments = Nutriments(calories = 553.0, proteins = 18.0, carbs = 30.0, fat = 44.0)),
        Food(barcode = "210", name = "Semillas de Chía", nutriments = Nutriments(calories = 486.0, proteins = 17.0, carbs = 42.0, fat = 31.0)),
        Food(barcode = "211", name = "Semillas de Lino", nutriments = Nutriments(calories = 534.0, proteins = 18.0, carbs = 29.0, fat = 42.0)),
        Food(barcode = "212", name = "Semillas de Girasol", nutriments = Nutriments(calories = 584.0, proteins = 21.0, carbs = 20.0, fat = 51.0)),
        Food(barcode = "213", name = "Pipas de Calabaza", nutriments = Nutriments(calories = 559.0, proteins = 30.0, carbs = 10.0, fat = 49.0)),
        Food(barcode = "214", name = "Cacahuetes Tostados", nutriments = Nutriments(calories = 567.0, proteins = 25.8, carbs = 16.0, fat = 49.2)),
        Food(barcode = "215", name = "Castañas Asadas", nutriments = Nutriments(calories = 196.0, proteins = 2.0, carbs = 44.0, fat = 2.2)),

    // --- BEBIDAS, SALSAS Y OTROS (216 - 230) ---
        Food(barcode = "216", name = "Agua Mineral", nutriments = Nutriments(calories = 0.0, proteins = 0.0, carbs = 0.0, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "217", name = "Té Verde (Bebida)", nutriments = Nutriments(calories = 1.0, proteins = 0.0, carbs = 0.2, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "218", name = "Cerveza Extra", nutriments = Nutriments(calories = 43.0, proteins = 0.5, carbs = 3.6, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "219", name = "Vino Tinto", nutriments = Nutriments(calories = 85.0, proteins = 0.0, carbs = 2.6, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "220", name = "Ketchup", nutriments = Nutriments(calories = 112.0, proteins = 1.2, carbs = 26.0, fat = 0.1)),
        Food(barcode = "221", name = "Mayonesa Tradicional", nutriments = Nutriments(calories = 680.0, proteins = 1.0, carbs = 1.0, fat = 75.0)),
        Food(barcode = "222", name = "Mostaza Dijon", nutriments = Nutriments(calories = 66.0, proteins = 4.4, carbs = 5.0, fat = 4.0)),
        Food(barcode = "223", name = "Salsa de Soja", nutriments = Nutriments(calories = 53.0, proteins = 9.0, carbs = 4.9, fat = 0.6, quantityUnit = "ml")),
        Food(barcode = "224", name = "Vinagre de Módena", nutriments = Nutriments(calories = 88.0, proteins = 0.5, carbs = 17.0, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "225", name = "Miel de Abeja", nutriments = Nutriments(calories = 304.0, proteins = 0.3, carbs = 82.0, fat = 0.0)),
        Food(barcode = "226", name = "Azúcar Blanco", nutriments = Nutriments(calories = 387.0, proteins = 0.0, carbs = 100.0, fat = 0.0)),
        Food(barcode = "227", name = "Refresco de Cola Sugafree", nutriments = Nutriments(calories = 0.3, proteins = 0.0, carbs = 0.0, fat = 0.0, quantityUnit = "ml")),
        Food(barcode = "228", name = "Patatas Fritas de Bolsa", nutriments = Nutriments(calories = 536.0, proteins = 7.0, carbs = 53.0, fat = 35.0)),
        Food(barcode = "229", name = "Pizza Prosciutto Congelada", nutriments = Nutriments(calories = 230.0, proteins = 10.5, carbs = 26.0, fat = 8.8)),
        Food(barcode = "230", name = "Galletas Tipo Digestiva", nutriments = Nutriments(calories = 480.0, proteins = 6.5, carbs = 62.0, fat = 21.0))

    )

    suspend fun generateData(months: Int = 2) = coroutineScope {
        val userId = sessionManager.getUserId() ?: "dev_user_id"
        // Usamos Clock de kotlinx.datetime explícitamente
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = today.minus(months, DateTimeUnit.MONTH)

        // 1. Asegurar que los alimentos base existen en la biblioteca en paralelo
        sampleFoods.map { food ->
            async {
                val foodWithId = food.copy(id = Uuid.random().toString())
                foodRepository.saveFood(foodWithId)
            }
        }.awaitAll()

        // 2. Poblar días
        var currentDate = startDate
        while (currentDate <= today) {
            val template = sessionManager.mealTemplate.value
            val dateForMeal = currentDate // Captura para la corrutina

            template.forEach { templateMeal ->
                // Decidimos aleatoriamente si esta comida tiene alimentos (80% de probabilidad)
                if (Random.nextFloat() > 0.2f) {
                    launch {
                        val numFoods = Random.nextInt(2, 6)
                        val mealFoods = mutableListOf<Food>()

                        repeat(numFoods) {
                            val baseFood = sampleFoods.random()
                            val quantity = Random.nextDouble(30.0, 150.0).round(1) ?: 100.0
                            val factor = quantity / 100.0

                            val snapshotFood = baseFood.copy(
                                id = Uuid.random().toString(),
                                nutriments = baseFood.nutriments?.copy(
                                    quantity = quantity.toString(),
                                    calories = baseFood.nutriments.calories?.let { (it * factor).round(2) },
                                    proteins = baseFood.nutriments.proteins?.let { (it * factor).round(2) },
                                    carbs = baseFood.nutriments.carbs?.let { (it * factor).round(2) },
                                    fat = baseFood.nutriments.fat?.let { (it * factor).round(2) }
                                )
                            )
                            mealFoods.add(snapshotFood)
                        }

                        val meal = templateMeal.copy(
                            id = Uuid.random().toString(),
                            foods = mealFoods
                        )

                        val mealEntity = meal.toEntity(userId, dateForMeal)
                        mealRepository.insertMealWithFood(
                            mealEntity = mealEntity,
                            snapshots = meal.toSnapshotEntities(mealEntity.id)
                        )
                    }
                }
            }
            currentDate = currentDate.plus(1, DateTimeUnit.DAY)
        }
    }
}
