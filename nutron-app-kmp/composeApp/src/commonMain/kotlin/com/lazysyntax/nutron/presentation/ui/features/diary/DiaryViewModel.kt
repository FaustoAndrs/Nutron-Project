package com.lazysyntax.nutron.presentation.ui.features.diary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazysyntax.nutron.domain.repository.FoodRepository
import com.lazysyntax.nutron.domain.repository.MealRepository
import com.lazysyntax.nutron.data.local.meal.toEntity
import com.lazysyntax.nutron.data.local.meal.toSnapshotEntities
import com.lazysyntax.nutron.data.remote.authentication.SessionManager
import com.lazysyntax.nutron.presentation.ui.features.diary.library.LibraryEvent
import com.lazysyntax.nutron.presentation.ui.features.diary.macros.MacrosEvent
import com.lazysyntax.nutron.presentation.ui.features.setUp.SetUpUiState
import com.lazysyntax.nutron.presentation.ui.features.setUp.composables.Calculator
import com.lazysyntax.nutron.presentation.ui.features.targets.DietPreset
import com.lazysyntax.nutron.presentation.ui.features.targets.TargetsUiState
import com.lazysyntax.nutron.presentation.ui.navigation.Navigator
import com.lazysyntax.nutron.presentation.ui.navigation.Route
import com.lazysyntax.nutron.domain.models.Food
import com.lazysyntax.nutron.domain.models.Meal
import com.lazysyntax.nutron.domain.models.Nutriments
import com.lazysyntax.nutron.domain.models.round
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant


class DiaryViewModel(
    private val foodRepository: FoodRepository,
    private val navigator: Navigator,
    private val mealRepository: MealRepository,
    private val sessionManager: SessionManager
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        DiaryUiState().copy(
            meals = sessionManager.mealTemplate.value,
            targets = sessionManager.getCurrentUserData().toTargetsUiState()
        )
    )
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        // Carga los datos reales de la base de datos para hoy al iniciar
        loadMealsForDate(_uiState.value.date)
    }

    private fun SetUpUiState.toTargetsUiState(): TargetsUiState {
        val w = weight.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        val a = age.toIntOrNull() ?: 0

        val bmiValue = Calculator.calculateBMI(w, h)
        val fatPercentage = Calculator.calculateFatPercentage(bmiValue, a, gender)
        val bmrValue = Calculator.calculateBMR(w, h, a, gender, fatPercentage, formula)
        val getValue = Calculator.calculateGET(bmrValue, activity.factor)
        val ebValue = Calculator.calculateEB(getValue, goal.objective.toString())

        return TargetsUiState(
            dailyKcal = "${ebValue.toInt()}",
            diet = DietPreset.fromLabel(this.diet).toDiet()
        )
    }

    fun onDiaryEvent(diaryEvent: DiaryEvent) {
        when (diaryEvent) {
            is DiaryEvent.OnClickAddProduct -> {
                onClickAddProductToMeal(diaryEvent.meal)
            }

            DiaryEvent.OnClickChangeDate -> onClicChangeDate()
            DiaryEvent.OnDismissDatePicker -> onDismissDatePicker()
            is DiaryEvent.OnDateSelected -> onDateSelected(diaryEvent.dateMillis)
            DiaryEvent.OnClickPreviousDay -> onMoveDay(-1)
            DiaryEvent.OnClickNextDay -> onMoveDay(1)
            is DiaryEvent.OnAddMeal -> onAddMealToTemplate(diaryEvent.meal)
            is DiaryEvent.OnDeleteMeal -> onDeleteMealToTemplate(diaryEvent.meal)
        }
    }

    fun onLibraryEvent(libraryEvent: LibraryEvent) {
        when (libraryEvent) {
            is LibraryEvent.BarcodeChanged -> onBarcodeFieldChange(libraryEvent.barcode)
            LibraryEvent.OnClickSearchBarcode -> onSearchBarcode()
            LibraryEvent.OnClickSearchProduct -> onSearchProduct()
            is LibraryEvent.ProductNameChanged -> onProductNameFieldChange(libraryEvent.productName)
            is LibraryEvent.ProductSelected -> onFoodSelected(libraryEvent.product)
            LibraryEvent.OnClickBack -> {
                onBack()
                _uiState.update {
                    it.copy(
                        libraryUiState = it.libraryUiState.copy(
                            foodResult = null, foodListResult = null
                        )
                    )
                }
            }

            is LibraryEvent.SelectedMeal -> setSelectedMeal(libraryEvent.meal)
        }
    }

    fun onMacrosEvent(macrosEvent: MacrosEvent) {
        when (macrosEvent) {
            MacrosEvent.OnClickBack -> onBack()
            is MacrosEvent.QuantityChanged -> {
                onQuantityFieldChange(macrosEvent.quantity.toDouble())
            }

            MacrosEvent.OnclickSave -> {
                addFood()
                onBack()
            }
        }


    }

    //---------------- Diary methods---------------------------//
    private fun onDeleteMealToTemplate(meal: Meal) {
        sessionManager.addMealToTemplate(meal)
    }

    private fun onAddMealToTemplate(meal: Meal) {
        sessionManager.removeMealFromTemplate(meal.name)
    }


    private fun onClicChangeDate() {

        _uiState.update { it.copy(isDatePickerVisible = true) }
    }

    private fun onDismissDatePicker() {
        _uiState.update { it.copy(isDatePickerVisible = false) }
    }

    private fun onDateSelected(dateMillis: Long?) {

        dateMillis?.let { millis ->

            val date = Instant.fromEpochMilliseconds(millis).toLocalDateTime(TimeZone.UTC).date
            println("ON DATE SELECTED : $date \n millis: $dateMillis ")
            _uiState.update { it.copy(date = date, isDatePickerVisible = false) }
            loadMealsForDate(date)
        } ?: _uiState.update { it.copy(isDatePickerVisible = false) }
    }


    fun onClickAddProductToMeal(meal: Meal) {
        setSelectedMeal(meal)
        navigator.navigateTo(Route.Library)
    }


    private fun loadMealsForDate(date: LocalDate) {
        viewModelScope.launch {
            // 1. Obtener comidas guardadas y la plantilla actual
            val savedMeals = mealRepository.getMealsByDate(date)
            val templateMeals = sessionManager.mealTemplate.value

            // 2. Combinar: usamos las comidas guardadas si existen para ese nombre, sino la de la plantilla
            val mergedMeals = templateMeals.map { templateMeal ->
                savedMeals.find { it.name == templateMeal.name } ?: templateMeal
            }

            // 3. Añadir comidas que estén en la DB pero no en la plantilla actual (comidas extra)
            val extraMeals = savedMeals.filter { saved ->
                templateMeals.none { it.name == saved.name }
            }

            _uiState.update { it.copy(meals = mergedMeals + extraMeals) }
        }
    }

    private fun onMoveDay(days: Int) {
        val currentDate = _uiState.value.date
        val newDate = if (days >= 0) {
            currentDate.plus(days, DateTimeUnit.DAY)
        } else {
            currentDate.minus(-days, DateTimeUnit.DAY)
        }

        _uiState.update { it.copy(date = newDate) }
        loadMealsForDate(newDate)
    }


    fun setSelectedMeal(meal: Meal) {
        _uiState.update { it.copy(selectedMeal = meal) }
    }

    //----------------Library methods---------------------------//
    private fun onBack() {
        navigator.goBack()
    }


    // Alimentos guardados localmente expuestos como StateFlow
    val savedFoods: StateFlow<List<Food>> = foodRepository.getSavedFoods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onProductNameFieldChange(productName: String) {
        _uiState.update {
            it.copy(
                libraryUiState = it.libraryUiState.copy(productName = productName)
            )
        }
    }

    fun onBarcodeFieldChange(barcode: String) {
        _uiState.update {
            it.copy(
                libraryUiState = it.libraryUiState.copy(
                    barcode = barcode
                )
            )
        }
    }

    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    fun onSearchBarcode() {
        val barcode = _uiState.value.libraryUiState.barcode

        viewModelScope.launch {
            val product = foodRepository.fetchFoodByBarcode(barcode)
            if (product != null) {
                // Aseguramos que el barcode esté presente en el objeto y generamos un ID si no lo tiene
                val productWithId = product.copy(
                    id = product.id ?: kotlin.uuid.Uuid.random().toString(),
                    barcode = product.barcode ?: barcode,
                    nutriments = product.nutriments?.copy(
                        quantity = product.nutriments.quantity ?: "100",
                        quantityUnit = product.nutriments.quantityUnit ?: "g"
                    ) ?: Nutriments()
                )

                _uiState.update {
                    it.copy(
                        libraryUiState = it.libraryUiState.copy(
                            foodResult = productWithId
                        ),
                        macrosUiState = it.macrosUiState.copy(
                            food = productWithId,
                            editedFood = productWithId
                        )
                    )
                }

                // Se guarda en ROOM
                onSaveFood(productWithId)

                navigator.navigateTo(Route.Macros)
            }
        }
    }

    fun onSearchProduct() {
        val productName = _uiState.value.libraryUiState.productName

        viewModelScope.launch {
            val foods = foodRepository.searchFoodByName(productName)
            _uiState.update {
                it.copy(
                    libraryUiState = it.libraryUiState.copy(
                        foodListResult = foods
                    )
                )
            }
        }
    }

    // Al seleccionar un alimento de la búsqueda para guardarlo localmente
    @OptIn(kotlin.uuid.ExperimentalUuidApi::class)
    fun onFoodSelected(food: Food) {
        viewModelScope.launch {
            val foodWithId = food.copy(
                id = food.id ?: kotlin.uuid.Uuid.random().toString(),
                nutriments = food.nutriments?.copy(
                    quantity = food.nutriments.quantity ?: "100",
                    quantityUnit = food.nutriments.quantityUnit ?: "g"
                ) ?: Nutriments()
            )
            onSaveFood(foodWithId)
            _uiState.update {
                it.copy(
                    macrosUiState = it.macrosUiState.copy(
                        food = foodWithId,
                        editedFood = foodWithId
                    )
                )
            }
            navigator.navigateTo(Route.Macros)
        }
    }

    fun onSaveFood(food: Food) {
        viewModelScope.launch {
            foodRepository.saveFood(food)
        }
    }

    fun onDeleteFood(code: String) {
        viewModelScope.launch {
            foodRepository.deleteFood(code)
        }
    }

    fun onCreateMeal(name: String, foods: List<Food>) {
        viewModelScope.launch {
            mealRepository.createMeal(name, foods)
        }
    }


    //---------------- Macros methods---------------------------//

    fun onQuantityFieldChange(quantity: Double) {
        _uiState.update { state ->
            // Obtenemos el alimento base (que contiene los macros cada 100g)
            val baseFood = state.macrosUiState.food ?: return@update state
            val baseNutriments = baseFood.nutriments ?: Nutriments()

            // Calculamos el factor de proporción (ej: si quantity es 200g, el factor es 2.0)
            val factor = quantity / 100.0

            // Creamos los nuevos nutriments proporcionales
            val updatedNutriments = baseNutriments.copy(
                quantity = quantity.toString(),
                calories = baseNutriments.calories?.let { (it * factor).round(2) },
                proteins = baseNutriments.proteins?.let { (it * factor).round(2) },
                carbs = baseNutriments.carbs?.let { (it * factor).round(2) },
                fat = baseNutriments.fat?.let { (it * factor).round(2) },
                saturatedFat = baseNutriments.saturatedFat?.let { (it * factor).round(2) },
                sugars = baseNutriments.sugars?.let { (it * factor).round(2) },
                salt = baseNutriments.salt?.let { (it * factor).round(2) }
            )

            // Actualizamos editedFood dentro de macrosUiState
            state.copy(
                macrosUiState = state.macrosUiState.copy(
                    editedFood = baseFood.copy(nutriments = updatedNutriments)
                )
            )
        }


    }


    suspend fun onSaveMealWithFood(meal: Meal) {
        // 1. Buscamos si ya existe una comida con este nombre para hoy
        val existingMeals = mealRepository.getMealsByDate(_uiState.value.date)
        val existingMeal = existingMeals.find { it.name == meal.name }

        // 2. Usamos el ID existente si lo hay, para evitar duplicados (UPDATE en lugar de INSERT)
        val mealToSave = if (existingMeal != null) {
            meal.copy(id = existingMeal.id)
        } else {
            meal
        }

        val mealEntity = mealToSave.toEntity(
            userId = sessionManager.getUserId() ?: "no_user_id",
            date = _uiState.value.date,
        )
        
        // 3. Persistimos (el DAO ya maneja el borrado de snapshots antiguos en la transacción)
        mealRepository.insertMealWithFood(
            mealEntity = mealEntity,
            snapshots = mealToSave.toSnapshotEntities(mealEntity.id)
        )
        
        // 4. Refrescamos los datos
        loadMealsForDate(_uiState.value.date)
    }

    /**
     * Añade el alimento actual a la lista de alimentos de la comida.
     */
    fun addFood() {
        val currentState = _uiState.value
        val selectedMeal = currentState.selectedMeal ?: return
        val selectedFood = currentState.macrosUiState.editedFood

        // 1. Obtener la comida actual del estado de la UI (que ya tiene los alimentos anteriores)
        val currentMealInList = currentState.meals?.find { it.name == selectedMeal.name }
        
        // 2. Combinar la lista de alimentos (los que ya tenía + el nuevo)
        val updatedFoods = (currentMealInList?.foods ?: emptyList()) + selectedFood
        val updatedMeal = (currentMealInList ?: selectedMeal).copy(foods = updatedFoods)

        // 3. Actualizamos el estado de la UI de forma optimista
        val updatedMeals = currentState.meals?.map { meal ->
            if (meal.name == updatedMeal.name) updatedMeal else meal
        }

        _uiState.update {
            it.copy(
                meals = updatedMeals,
                selectedMeal = null
            )
        }

        // 4. Persistir en la base de datos (onSaveMealWithFood se encargará de usar el ID correcto)
        viewModelScope.launch {
            onSaveMealWithFood(updatedMeal)
        }
    }

    /**
     * Función de utilidad para actualizar campos dentro de Nutriments de forma segura.
     */
    //        updateNutriments { copy(proteins = proteins) }
    private fun updateNutriments(updateBlock: Nutriments.() -> Nutriments) {

        _uiState.update { state ->
            val currentMacrosState = state.macrosUiState
            state.copy(
                macrosUiState = currentMacrosState.copy(
                    editedFood = currentMacrosState.editedFood.copy(
                        nutriments = (currentMacrosState.editedFood.nutriments
                            ?: Nutriments()).updateBlock()
                    )
                )
            )
        }
    }
}
