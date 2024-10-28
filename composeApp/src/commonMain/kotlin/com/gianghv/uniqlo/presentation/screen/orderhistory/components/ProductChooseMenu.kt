package com.gianghv.uniqlo.presentation.screen.orderhistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.Menu
import com.composables.core.MenuButton
import com.composables.core.MenuContent
import com.composables.core.MenuItem
import com.composables.core.rememberMenuState
import com.gianghv.uniqlo.domain.OrderDetail
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_dark_uniqlo

@Composable
fun ProductChooseMenu(modifier: Modifier = Modifier, selected: OrderDetail?, list: List<OrderDetail>, onSelected: (OrderDetail) -> Unit, screenWidth: Dp) {
    if (list.isNotEmpty()) {
        val state = rememberMenuState(expanded = false)

        Menu(state = state, modifier = modifier) {
            MenuButton(
                Modifier.clip(RoundedCornerShape(6.dp)).border(0.5.dp, Color(0xFFBDBDBD), RoundedCornerShape(6.dp))
            ) {
                val selectedName = selected?.variation?.name

                Box(modifier = Modifier.wrapContentSize()) {
                    if (selectedName != null) {
                        BasicText(selectedName, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(8.dp))
                    } else {
                        BasicText("Choose Product", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(8.dp))
                    }
                }

            }

            MenuContent(
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).background(Color.White).padding(4.dp),
            ) {
                list.forEachIndexed { _, option ->
                    MenuItem(modifier = Modifier.clip(RoundedCornerShape(4.dp)), onClick = {
                        state.expanded = false
                        onSelected(option)
                    }) {
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(4.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row (modifier = Modifier) {
                                val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
                                    placeholder(Res.drawable.ic_dark_uniqlo)
                                    crossfade()
                                })

                                AsyncImage(
                                    uri = option.variation?.image ?: "",
                                    modifier = modifier.size(screenWidth * 0.15f).clip(RoundedCornerShape(10.dp)),
                                    state = isImageLoadedSuccessfully,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )

                                BasicText(
                                    text = option.variation?.name ?: "", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}
