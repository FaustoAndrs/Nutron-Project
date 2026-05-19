package com.lazysyntax.nutron.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.round

@Serializable
data class Food(
    val id: String? = null,
    @SerialName("code") val barcode: String? = null,
    @SerialName("product_name") val name: String? = null,
    @SerialName("product_name_es") val nameEs: String? = null,
    @SerialName("product_name_en") val nameEn: String? = null,
    //per serving o 100 g/ml
    @SerialName("nutrition_data_per") val nutritionDataPer: String? = null,
    val nutriments: Nutriments? = null,
    @SerialName("nutriscore_grade") val nutriscoreGrade: String? = null,
    @SerialName("brands") val brands: String? = null
)
@Serializable
data class Nutriments(
     val quantity: String? = "100",
     val quantityUnit: String? = "g",
    @SerialName("energy-kcal_100g") val calories: Double? = null,
    @SerialName("proteins_100g") val proteins: Double? = null,
    @SerialName("carbohydrates_100g") val carbs: Double? = null,
    @SerialName("fat_100g") val fat: Double? = null,
    @SerialName("saturated-fat_100g") val saturatedFat: Double? = null,
    @SerialName("sugars_100g") val sugars: Double? = null,
    @SerialName("salt_100g") val salt: Double? = null
)

fun Double?.round(decimals: Int = 2): Double? {
    if (this == null) return null
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}
