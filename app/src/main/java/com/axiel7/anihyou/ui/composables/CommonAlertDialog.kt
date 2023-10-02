package com.axiel7.anihyou.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.R
import com.axiel7.anihyou.data.model.base.Localizable

@Composable
fun <T : Localizable> DialogWithRadioSelection(
    values: Array<T>,
    defaultValue: T?,
    title: String? = null,
    isDeselectable: Boolean = false,
    onConfirm: (T?) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    var selectedValue by remember { mutableStateOf(defaultValue) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedValue) }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { if (title != null) Text(text = title) },
        text = {
            LazyColumn(
                modifier = Modifier.sizeIn(
                    maxHeight = (configuration.screenHeightDp - 48).dp
                )
            ) {
                items(values) { value ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedValue = if (isDeselectable) {
                                    if (selectedValue != value) value else null
                                } else value
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedValue == value,
                            onClick = {
                                selectedValue = if (isDeselectable) {
                                    if (selectedValue != value) value else null
                                } else value
                            }
                        )
                        Text(text = value.localized())
                    }
                }
            }
        }
    )
}

@Composable
fun <T : Localizable> DialogWithCheckboxSelection(
    values: Array<T>,
    defaultValues: Array<T>,
    title: String? = null,
    onConfirm: (List<T>) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val selectedValues = remember { mutableStateListOf<T>() }

    LaunchedEffect(defaultValues) {
        selectedValues.addAll(defaultValues)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selectedValues.toList()) }
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
        title = { if (title != null) Text(text = title) },
        text = {
            LazyColumn(
                modifier = Modifier.sizeIn(
                    maxHeight = (configuration.screenHeightDp - 48).dp
                )
            ) {
                items(values) { value ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selectedValues.contains(value)) selectedValues.remove(value)
                                else selectedValues.add(value)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = selectedValues.contains(value),
                            onCheckedChange = { checked ->
                                if (checked) selectedValues.add(value)
                                else selectedValues.remove(value)
                            }
                        )
                        Text(text = value.localized())
                    }
                }
            }
        }
    )
}