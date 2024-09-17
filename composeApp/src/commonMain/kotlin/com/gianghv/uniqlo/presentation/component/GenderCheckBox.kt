package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxGroup(
    checkItems: List<String>
) {
    val checkedIndex = remember { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.padding(16.dp).wrapContentHeight()) {
        items(checkItems) {
            checkItem ->
            val index = checkItems.indexOf(checkItem)
            Row {
                Checkbox(
                    checked =checkedIndex.value == index,
                    onCheckedChange = { checked ->
                        if (checked) {
                            checkedIndex.value = index
                        } else {
                            checkedIndex.value = -1
                        }
                    }
                )
                Text(text = checkItem, modifier = Modifier.padding(start = 8.dp))
            }
        }

    }
}
