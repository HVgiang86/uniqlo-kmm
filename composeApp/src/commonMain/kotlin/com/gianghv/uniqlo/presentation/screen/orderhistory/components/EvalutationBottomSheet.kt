package com.gianghv.uniqlo.presentation.screen.orderhistory.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.Icon
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.domain.OrderDetail
import com.gianghv.uniqlo.domain.OrderHistory
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField
import com.gianghv.uniqlo.presentation.component.InputWrapper
import com.gianghv.uniqlo.presentation.component.RedFilledTextButton
import com.gianghv.uniqlo.presentation.screen.orderhistory.toOrderStatus
import com.gianghv.uniqlo.presentation.screen.orderhistory.toText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EvaluationCreateBottomSheet(
    state: ModalBottomSheetState? = null,
    order: OrderHistory,
    screenHeight: Dp,
    screenWidth: Dp,
    onCreateEvaluation: ((Long, Double, String) -> Unit) = { productId: Long, star: Double, content: String -> },
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )

    val selectedProduct = remember {
        mutableStateOf<OrderDetail?>(null)
    }

    val errorThrowable = remember { mutableStateOf<Throwable?>(null) }

    selectedProduct.value = null

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).imePadding().shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).fillMaxWidth().wrapContentHeight()
                .heightIn(max = screenHeight * 0.8f),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).align(Alignment.CenterHorizontally).background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp).height(4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OrderHistoryItem(order = order, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(32.dp))

                Text("Đánh giá sản phẩm", style = MaterialTheme.typography.titleSmall, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Chọn sản phẩm", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                ProductChooseMenu(modifier = Modifier, selected = selectedProduct.value, list = order.details ?: emptyList(), onSelected = {
                    selectedProduct.value = it
                }, screenWidth = screenWidth)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Đánh giá", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                var sliderPosition by remember { mutableStateOf(1f) }
                val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }

                Text(text = sliderPosition.toInt().toString(), style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                Slider(modifier = Modifier.fillMaxWidth().padding(top = 8.dp).semantics { contentDescription = "Rating" },
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    steps = 3,
                    valueRange = 1f..5f,
                    interactionSource = interactionSource,
                    thumb = {
                        Label(
                            label = {
                                PlainTooltip(modifier = Modifier.sizeIn(45.dp, 25.dp).wrapContentWidth()) {
                                    Text(sliderPosition.toInt().toString())
                                }
                            }, interactionSource = interactionSource
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = Color.Red
                            )
                        }
                    })

                val content = rememberSaveable {
                    mutableStateOf("")
                }

                Text("Nội dung đánh giá", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(top = 8.dp)) {
                    val inputWrapper = remember { mutableStateOf(InputWrapper()) }

                    AppOutlinedTextField(modifier = Modifier.height((52).dp), initialValue = content.value, onValueChange = {
                        content.value = it
                    }, onMessageSent = {
                        content.value = it
                    }, maxLines = 1, placeholder = "Nội dung", imeAction = ImeAction.Done, inputWrapper = inputWrapper, validator = {
                        validateNotEmpty(it)
                    })
                }

                Spacer(modifier = Modifier.height(16.dp))

                RedFilledTextButton(onClick = {
                    //validate
                    val pId = selectedProduct.value?.variation?.productId

                    if (pId == null) {
                        errorThrowable.value = Exception("Vui lòng chọn sản phẩm")
                        return@RedFilledTextButton
                    }

                    if (content.value.isBlank()) {
                        errorThrowable.value = Exception("Vui lòng nhập nội dung đánh giá")
                        return@RedFilledTextButton
                    }

                    onCreateEvaluation(pId, sliderPosition.toDouble(), content.value)

                    errorThrowable.value = null
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Gửi đánh giá", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppErrorDialog(throwable = errorThrowable.value, onDismissRequest = {
                    errorThrowable.value = null
                })
            }
        }
    }
}

@Composable
private fun OrderHistoryItem(modifier: Modifier = Modifier, order: OrderHistory) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                Text("#${order.name}", style = MaterialTheme.typography.titleMedium, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                val status = order.status?.toOrderStatus()?.toText()
                Text(">$status", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        }
    }
}
