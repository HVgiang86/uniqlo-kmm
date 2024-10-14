package com.gianghv.uniqlo.presentation.screen.searchresult.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.rememberMenuState
import com.gianghv.uniqlo.presentation.screen.productdetail.components.VariationSize

@Composable
fun FilterSize(selectedSize: VariationSize?, sizeList: List<VariationSize> = emptyList(), onSizeSelected: (VariationSize?) -> Unit) {
    if (sizeList.isNotEmpty()) {
        val state = rememberMenuState(expanded = false)

        Menu(state = state) {
            MenuButton(
                Modifier.clip(RoundedCornerShape(6.dp)).border(0.5.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText("Size ${selectedSize?.name ?: ""}", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            MenuContent(
                modifier = Modifier.width(80.dp).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).background(Color.White).padding(4.dp),
            ) {
                MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                    state.expanded = false
                    onSizeSelected(null)
                }) {
                    BasicText("All", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
                }

                sizeList.forEachIndexed { _, option ->
                    MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                        state.expanded = false
                        onSizeSelected(option)
                    }) {
                        if (selectedSize == option) BasicText(
                            option.name,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp).background(MaterialTheme.colorScheme.primary)
                        )
                        else BasicText(option.name, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilterColor(selectedColor: String?, colorList: List<String> = emptyList(), onColorSelected: (String?) -> Unit) {
    if (colorList.isNotEmpty()) {
        val state = rememberMenuState(expanded = false)

        Menu(state = state) {
            MenuButton(
                Modifier.clip(RoundedCornerShape(6.dp)).border(0.5.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BasicText("Color ${selectedColor ?: ""}", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.width(4.dp))
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }

            MenuContent(
                modifier = Modifier.width(80.dp).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).background(Color.White).padding(4.dp),
            ) {
                MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                    state.expanded = false
                    onColorSelected(null)
                }) {
                    BasicText("All", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
                }

                colorList.forEachIndexed { _, option ->
                    MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                        state.expanded = false
                        onColorSelected(option)
                    }) {
                        if (selectedColor.equals(option, ignoreCase = true)) BasicText(
                            option, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp).background(MaterialTheme.colorScheme.primary)
                        )
                        else BasicText(option, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FilterPrice(isDesc: Boolean? = false, isDescSelected: () -> Unit, isAscSelected: () -> Unit) {
    val state = rememberMenuState(expanded = false)

    Menu(state = state) {
        MenuButton(
            Modifier.clip(RoundedCornerShape(6.dp)).border(0.5.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val label = when (isDesc) {
                    true -> "Desc"
                    false -> "Asc"
                    null -> ""
                }

                BasicText("Price $label", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        MenuContent(
            modifier = Modifier.width(80.dp).border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).background(Color.White).padding(4.dp),
        ) {
            MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                state.expanded = false
                isAscSelected()
            }) {
                BasicText("Asc", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
            }

            MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                state.expanded = false
                isDescSelected()
            }) {
                BasicText("Desc", modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 4.dp))
            }

        }
    }
}
