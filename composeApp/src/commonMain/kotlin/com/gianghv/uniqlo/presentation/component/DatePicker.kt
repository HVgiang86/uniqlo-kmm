package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.constant.DD_MM_YYYY
import com.gianghv.uniqlo.constant.YYYY_MM_DD
import com.gianghv.uniqlo.util.dateformat.DateTime
import com.gianghv.uniqlo.util.ext.getCurrentDate
import com.gianghv.uniqlo.util.ext.toLocalDate
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    modifier: Modifier = Modifier,
    title: String? = "SELECT DATE",
    state: DatePickerState = rememberDatePickerState(),
    colors: DatePickerColors = DatePickerDefaults.colors(),
    onDateSelected: (LocalDate) -> Unit = {},
    onDismiss: () -> Unit = {},
    allowedDateValidator: (LocalDate) -> Boolean = { true },
    showModeToggle: Boolean = true,
) {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        val confirmEnabled = remember { derivedStateOf { state.selectedDateMillis != null } }
        DatePickerDialog(onDismissRequest = {
            openDialog.value = false
        }, confirmButton = {
            TextButton(onClick = {
                openDialog.value = false
                state.selectedDateMillis?.let {
                    onDateSelected(it.toLocalDate())
                }

            }, enabled = confirmEnabled.value) { Text("OK") }
        }, dismissButton = {
            TextButton(onClick = {
                openDialog.value = false
                onDismiss()
            }) { Text("Cancel") }
        }, shape = RoundedCornerShape(8.dp)) {
            DatePicker(modifier = Modifier.padding(16.dp), state = state, colors = colors, showModeToggle = showModeToggle, title = {
                title?.let {
                    Text(text = it, style = MaterialTheme.typography.titleMedium)
                }
            }, headline = {
                val dateFormatter = object : DatePickerFormatter {
                    override fun formatDate(dateMillis: Long?, locale: CalendarLocale, forContentDescription: Boolean): String {
                        if (dateMillis == null) {
                            val current = getCurrentDate()
                            return DateTime.getFormattedDate(current.toString(), YYYY_MM_DD, DD_MM_YYYY) ?: "Selected date"
                        }

                        val localDate = dateMillis.toLocalDate()
                        return DateTime.getFormattedDate(localDate.toString(), YYYY_MM_DD, DD_MM_YYYY) ?: "Selected date"
                    }

                    override fun formatMonthYear(monthMillis: Long?, locale: CalendarLocale): String {
                        if (monthMillis == null) {
                            val current = getCurrentDate()
                            return "${current.month}/${current.year}"
                        }

                        val localDate = monthMillis.toLocalDate()
                        return "${localDate.month}/${localDate.year}"
                    }

                }
                DatePickerDefaults.DatePickerHeadline(state.selectedDateMillis, displayMode = DisplayMode.Picker, dateFormatter = dateFormatter)
            })
        }
    }
}
