package com.gianghv.uniqlo.presentation.screen.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composables.core.DragIndication
import com.composables.core.ModalBottomSheet
import com.composables.core.ModalBottomSheetState
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.presentation.component.AppOutlinedTextField

@Composable
fun SettingBottomSheet(
    state: ModalBottomSheetState? = null,
    onChangeRecommendationServer: (String) -> Unit,
    onChangeChatServer: (String) -> Unit
) {
    val show = state ?: rememberModalBottomSheetState(
        initialDetent = FullyExpanded, detents = listOf(Hidden, FullyExpanded)
    )


    ModalBottomSheet(state = show) {
        Sheet(
            modifier = Modifier.padding(top = 12.dp).shadow(8.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)).background(Color.White).widthIn(max = 640.dp).fillMaxWidth().imePadding(),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
            ) {
                DragIndication(
                    modifier = Modifier.padding(top = 22.dp).align(Alignment.CenterHorizontally).background(Color.Black.copy(0.4f), RoundedCornerShape(100))
                        .width(32.dp).height(4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("App Settings", style = MaterialTheme.typography.titleSmall, color = Color.Black)
                Text("Recommendation Server URL", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(70.dp),
                    placeholder = "Recommendation Server URL",
                    initialValue = WholeApp.RECOMMEND_BASE_URL,
                    onValueChange = {
                        WholeApp.RECOMMEND_BASE_URL = it
                        onChangeRecommendationServer(it)
                    },
                    onMessageSent = {
                        WholeApp.RECOMMEND_BASE_URL = it
                        onChangeRecommendationServer(it)
                    },
                    textStyle = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("AI Chat Server URL", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

                AppOutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp).height(70.dp),
                    placeholder = "AI Chat Server URL",
                    initialValue = WholeApp.CHAT_BASE_URL,
                    onValueChange = {
                        WholeApp.CHAT_BASE_URL = it
                        onChangeChatServer(it)
                    },
                    onMessageSent = {
                        WholeApp.CHAT_BASE_URL = it
                        onChangeChatServer(it)
                    },
                    textStyle = MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
