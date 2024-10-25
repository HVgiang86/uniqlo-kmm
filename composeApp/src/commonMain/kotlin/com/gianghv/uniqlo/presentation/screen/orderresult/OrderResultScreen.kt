package com.gianghv.uniqlo.presentation.screen.orderresult

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.BlackFilledTextButton
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uniqlo.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OrderResultScreen(isOrderSuccess: Boolean = false, navigateToHome: (MainScreenDestination) -> Unit) {
    Scaffold { scaffoldPadding ->
        var boxWidth by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(modifier = Modifier.fillMaxSize().padding(scaffoldPadding).padding(bottom = 80.dp).consumeWindowInsets(scaffoldPadding).systemBarsPadding()
            .onGloballyPositioned { layoutCoordinates ->
                val widthInPx = layoutCoordinates.size.width
                boxWidth = with(density) { widthInPx.toDp() }
            }) {

            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Center)) {
                var remainingTime by remember { mutableStateOf(5) }

                LaunchedEffect(Unit) {
                    while (remainingTime > 0) {
                        delay(1000L)
                        remainingTime--
                    }
                    navigateToHome(MainScreenDestination.Home)
                }

                val composition by rememberLottieComposition {
                    LottieCompositionSpec.JsonString(
                        if (isOrderSuccess) {
                            Res.readBytes("files/anim_order_success.json").decodeToString()
                        } else {
                            Res.readBytes("files/anim_order_fail.json").decodeToString()
                        }
                    )
                }

                Image(
                    painter = rememberLottiePainter(
                        composition = composition, iterations = Compottie.IterateForever
                    ), contentDescription = "Lottie animation", modifier = Modifier.size(boxWidth * 0.4f).align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(16.dp))
                if (isOrderSuccess) {
                    Text(text = "Đặt hàng thành công", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Text(text = "Đặt hàng thất bại", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                Spacer(modifier = Modifier.size(16.dp))

                BlackFilledTextButton(text = {
                    Text(text = "Về trang chủ sau ${remainingTime}s")
                }, onClick = {
                    navigateToHome(MainScreenDestination.Home)
                }, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
