package com.lazysyntax.nutron.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Food(
    @SerialName("code") val barcode: String? = null,
    @SerialName("product_name") val name: String? = null,
    @SerialName("product_name_es") val nameEs: String? = null,
    @SerialName("product_name_en") val nameEn: String? = null,
    @SerialName("nutrition_data_per") val nutritionDataPer: String? = null, //per serving o 100 g/ml
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