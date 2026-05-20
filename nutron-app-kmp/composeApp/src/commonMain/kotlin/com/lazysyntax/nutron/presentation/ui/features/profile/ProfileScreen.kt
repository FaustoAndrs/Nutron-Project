package com.lazysyntax.nutron.presentation.ui.features.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lazysyntax.nutron.presentation.ui.navigation.composables.NavBar
import com.lazysyntax.nutron.presentation.ui.navigation.composables.TopAppBarCommon
import kotlinx.coroutines.launch
import nutron.composeapp.generated.resources.Res
import nutron.composeapp.generated.resources.profile_balance_text_tooltip
import nutron.composeapp.generated.resources.profile_balance_tooltip
import nutron.composeapp.generated.resources.profile_bmi_text_tooltip
import nutron.composeapp.generated.resources.profile_bmi_tooltip
import nutron.composeapp.generated.resources.profile_body_water_label
import nutron.composeapp.generated.resources.profile_fat_label
import nutron.composeapp.generated.resources.profile_fat_text_tooltip
import nutron.composeapp.generated.resources.profile_fat_tooltip
import nutron.composeapp.generated.resources.profile_gender_label
import nutron.composeapp.generated.resources.profile_get_text_tooltip
import nutron.composeapp.generated.resources.profile_get_tooltip
import nutron.composeapp.generated.resources.profile_height_label
import nutron.composeapp.generated.resources.profile_mbr_label
import nutron.composeapp.generated.resources.profile_section_details
import nutron.composeapp.generated.resources.profile_section_resume
import nutron.composeapp.generated.resources.profile_show_progres
import nutron.composeapp.generated.resources.profile_tdee_label
import nutron.composeapp.generated.resources.profile_weight_label
import nutron.composeapp.generated.resources.title_profile
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val profileState by profileViewModel.uiState.collectAsState()
    ProfileContent(
        uiState = profileState,
        onNavigateToStatistics = { profileViewModel.onNavigateToStatistics() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onNavigateToStatistics: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBarCommon(stringResource(Res.string.title_profile)) },
        bottomBar = { NavBar() },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                ProfileSectionHeader(stringResource(Res.string.profile_section_resume))
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatHighlightCard(
                        label = "BMI",
                        value = uiState.bodyMassIndex,
                        unit = "",
                        titleTooltip = stringResource(Res.string.profile_bmi_tooltip),
                        textTooltip =  stringResource(Res.string.profile_bmi_text_tooltip),
                        modifier = Modifier.weight(1f)
                    )
                    StatHighlightCard(
                        label = stringResource(Res.string.profile_fat_label),
                        value = uiState.bodyFatPercentage,
                        unit = "%",
                        titleTooltip = stringResource(Res.string.profile_fat_tooltip),
                        textTooltip =  stringResource(Res.string.profile_fat_text_tooltip),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatHighlightCard(
                        label = stringResource(Res.string.profile_tdee_label),
                        value = uiState.gastoEnergeticoTotal,
                        unit = "kcal",
                        titleTooltip = stringResource(Res.string.profile_get_tooltip),
                        textTooltip = stringResource(Res.string.profile_get_text_tooltip),
                        modifier = Modifier.weight(1f)
                    )
                    StatHighlightCard(
                        label = "Balance",
                        value = uiState.energeticBalance,
                        unit = "kcal",
                        titleTooltip = stringResource(Res.string.profile_balance_tooltip),
                        textTooltip = stringResource(Res.string.profile_balance_text_tooltip),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onNavigateToStatistics,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(Res.string.profile_show_progres))
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                ProfileSectionHeader(stringResource(Res.string.profile_section_details))
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column {
                        ProfileDetailRow(stringResource(Res.string.profile_height_label), "${uiState.height} cm")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                        ProfileDetailRow(stringResource(Res.string.profile_weight_label), "${uiState.weight} kg")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                        ProfileDetailRow(stringResource(Res.string.profile_gender_label), uiState.gender)
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                        ProfileDetailRow(stringResource(Res.string.profile_mbr_label), "${uiState.basalMetabolicRate} kcal")
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                        ProfileDetailRow(stringResource(Res.string.profile_body_water_label), "${uiState.bodyWaterPercentage} %")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview
@Composable
fun PrevProfileContent(){
    val uiState = ProfileUiState(
        weight = "70",
    )
    ProfileContent(
        uiState = uiState,
        onNavigateToStatistics = {}
    )
}
@Composable
fun ProfileSectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatHighlightCard(
    label: String,
    value: String,
    unit: String,
    titleTooltip: String,
    textTooltip: String,
    modifier: Modifier = Modifier
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Above,
            spacingBetweenTooltipAndAnchor = 4.dp
        ),
        tooltip = {
            RichTooltip(
                title = { Text(titleTooltip) },
                text = { Text(textTooltip) }
            )
        },
        state = tooltipState
    ) {
        Card(
            modifier = modifier.clickable { scope.launch { tooltipState.show() } },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "info",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (unit.isNotEmpty()) {
                        Text(
                            text = " $unit",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileDetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
