package com.gianghv.uniqlo.presentation.screen.productdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.domain.Product

@Composable
fun EvaluationViewBottomSheet(
    state: ModalBottomSheetState? = null,
    product: Product,
    screenHeight: Dp,
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
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

                Text("Đánh giá sản phẩm", style = MaterialTheme.typography.titleSmall, color = Color.Black)

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), color = Color.Gray, thickness = 1.dp)

                repeat(2) {
                    if (it < (product.evaluations?.size ?: 0)) {
                        val evaluation = product.evaluations?.get(it)
                        evaluation?.let { it1 -> EvaluationCard(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp), evaluation = it1) }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
