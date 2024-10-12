package com.gianghv.uniqlo.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.Sheet
import com.composables.core.SheetDetent
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState

@Composable
fun MyBottomSheet(
) {
    val show = rememberModalBottomSheetState(
        initialDetent = Hidden,
        detents = listOf(Hidden, FullyExpanded)
    )

    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(4.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).widthIn(max = 640.dp).fillMaxWidth().imePadding(),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().height(1200.dp), contentAlignment = Alignment.TopCenter
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).background(Color.Black.copy(0.4f), RoundedCornerShape(100)).width(32.dp).height(4.dp)
                )
            }
        }
    }
}
