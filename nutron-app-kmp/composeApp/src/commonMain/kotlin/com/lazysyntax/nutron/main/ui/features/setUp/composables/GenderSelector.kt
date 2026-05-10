package com.lazysyntax.nutron.main.ui.features.setUp.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderSelector(
    sheetState: SheetState,
    scope: CoroutineScope,
    onGenderSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Select Gender",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            GenderOption("Male") {
                onGenderSelected("Male")
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onDismiss()
                }
            }
            GenderOption("Female") {
                onGenderSelected("Female")
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun GenderOption(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
    }
}