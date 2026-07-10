package com.asad.academytest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asad.academytest.R
import com.asad.academytest.ui.theme.AcademyTestTheme

/**
 * Modal sheet for creating a new item. Android equivalent of the SwiftUI `AddItemView`.
 *
 * `.sheet` + `.presentationDetents([.medium])` become a [ModalBottomSheet]; the trivial
 * `AddItemViewModel` (name / trimmedName / canSave) collapses into local Compose state,
 * which is idiomatic for screen-scoped input state. The finished name is raised via
 * [onSave]; [onDismiss] closes the sheet.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemSheet(
    onDismiss: () -> Unit,
    onSave: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
    ) {
        AddItemSheetContent(
            onCancel = onDismiss,
            onSave = { name ->
                onSave(name)
                onDismiss()
            },
        )
    }
}

@Composable
private fun AddItemSheetContent(
    onCancel: () -> Unit,
    onSave: (String) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    val trimmedName = name.trim()
    val canSave = trimmedName.isNotEmpty()
    val focusRequester = remember { FocusRequester() }

    fun save() {
        if (canSave) onSave(trimmedName)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Header: Cancel — title — Save, mirroring the original's toolbar buttons.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onCancel) {
                Text(stringResource(R.string.cancel))
            }
            Text(
                text = stringResource(R.string.add_title),
                style = MaterialTheme.typography.titleMedium,
            )
            TextButton(onClick = ::save, enabled = canSave) {
                Text(stringResource(R.string.save))
            }
        }

        Text(
            text = stringResource(R.string.new_item_section).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.field_name)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { save() }),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
        )
    }

    // Auto-focus the field when the sheet appears (the @FocusState analog).
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview(showBackground = true)
@Composable
private fun AddItemSheetContentPreview() {
    AcademyTestTheme {
        Surface {
            AddItemSheetContent(onCancel = {}, onSave = {})
        }
    }
}
