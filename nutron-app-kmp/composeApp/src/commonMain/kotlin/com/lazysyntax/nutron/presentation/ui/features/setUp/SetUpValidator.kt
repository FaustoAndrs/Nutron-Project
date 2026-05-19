package com.lazysyntax.nutron.presentation.ui.features.setUp

import com.lazysyntax.nutron.presentation.utilities.validation.ComposedValidator
import com.lazysyntax.nutron.presentation.utilities.validation.Validation
import com.lazysyntax.nutron.presentation.utilities.validation.Validator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.DoubleRangeValidator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.MaximumTextLengthValidator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.MinTextLengthValidator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.NonEmptyTextValidator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.IntRangeValidator
import com.lazysyntax.nutron.presentation.utilities.validation.validators.NumberValidator
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.activity_no_empty_validator
import nutron.composeapp.generated.resources.age_int_range_validator
import nutron.composeapp.generated.resources.age_max_length_validator
import nutron.composeapp.generated.resources.age_no_empty_validator
import nutron.composeapp.generated.resources.age_number_validator
import nutron.composeapp.generated.resources.weight_double_range_validator
import nutron.composeapp.generated.resources.formula_no_empty_validator
import nutron.composeapp.generated.resources.gender_no_empty_validator
import nutron.composeapp.generated.resources.goal_no_empty_validator
import nutron.composeapp.generated.resources.height_double_range_validator
import nutron.composeapp.generated.resources.height_min_length_validator
import nutron.composeapp.generated.resources.height_no_empty_validator
import nutron.composeapp.generated.resources.height_number_validator
import nutron.composeapp.generated.resources.weight_max_length_validator
import nutron.composeapp.generated.resources.weight_no_empty_validator
import nutron.composeapp.generated.resources.weight_number_validator

class SetUpValidator : Validator<SetUpUiState> {

    private val weightValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator( error = Res.string.weight_no_empty_validator))
        .add(NumberValidator( error = Res.string.weight_number_validator))
        .add(MaximumTextLengthValidator(maxLength = 3,error =  Res.string.weight_max_length_validator))
        .add(MinTextLengthValidator(minLength = 2, error =  Res.string.weight_max_length_validator))
        .add(DoubleRangeValidator(
            min = 30.0,
            max = 300.0,
            error = Res.string.weight_double_range_validator
        ))

    private val heightValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.height_no_empty_validator))
        .add(NumberValidator( error = Res.string.height_number_validator))
        .add(MaximumTextLengthValidator(maxLength = 3,error =  Res.string.weight_max_length_validator))
        .add(MinTextLengthValidator(minLength = 2, error =  Res.string.height_min_length_validator))
        .add(
            DoubleRangeValidator(
                min = 100.0,
                max = 250.0,
                error = Res.string.height_double_range_validator
            )
        )

    private val ageValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.age_no_empty_validator))
        .add(NumberValidator( error = Res.string.age_number_validator))
        .add(MaximumTextLengthValidator(maxLength = 2,error =  Res.string.age_max_length_validator))
        .add(
            IntRangeValidator(
                min = 10,
                max = 250,
                error = Res.string.age_int_range_validator
            )
        )
    private val genderValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.gender_no_empty_validator))
    private val activityValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.activity_no_empty_validator))
    private val goalValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.goal_no_empty_validator))
    private val formulaValidator = ComposedValidator<String>()
        .add(NonEmptyTextValidator(Res.string.formula_no_empty_validator))


    override fun validate(data: SetUpUiState): Validation {
        return SetUpUiStateValidation(
            weightValidation = weightValidator.validate(data.weight),
            heightValidation = heightValidator.validate(data.height),
            ageValidation = ageValidator.validate(data.age),
            genderValidation = genderValidator.validate(data.gender),
            activityValidation = activityValidator.validate(data.activity.name),
            goalValidation = goalValidator.validate(data.goal.name),
            formulaValidation = formulaValidator.validate(data.formula)
        )
    }
}
