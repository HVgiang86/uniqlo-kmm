package com.gianghv.uniqlo.presentation.screen.orderresult

import KottieAnimation
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
import kotlinx.coroutines.delay
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import uniqlo.composeapp.generated.resources.Res
import utils.KottieConstants

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
                var animation by remember { mutableStateOf("") }

                LaunchedEffect(Unit) {
                    if (isOrderSuccess) {
                        animation = Res.readBytes("files/anim_order_success.json").decodeToString()
                    } else {
                        animation = Res.readBytes("files/anim_order_fail.json").decodeToString()
                    }
                }

                var remainingTime by remember { mutableStateOf(5) }

                LaunchedEffect(Unit) {
                    while (remainingTime > 0) {
                        delay(1000L)
                        remainingTime--
                    }
                    navigateToHome(MainScreenDestination.Home)
                }

                val composition = rememberKottieComposition(
                    spec = KottieCompositionSpec.File(animation)
                )

                val animationState by animateKottieCompositionAsState(
                    composition = composition, restartOnPlay = true, isPlaying = true, iterations = KottieConstants.IterateForever
                )

                KottieAnimation(
                    composition = composition, progress = {
                        animationState.progress
                    }, modifier = Modifier.size(boxWidth * 0.4f).align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.size(16.dp))
                if (isOrderSuccess) {
                    Text(text = "Order Success", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Text(text = "Order Fail", style = MaterialTheme.typography.titleLarge, modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                Spacer(modifier = Modifier.size(16.dp))

                BlackFilledTextButton(text = {
                    Text(text = "Back to Home after ${remainingTime}s")
                }, onClick = {
                    navigateToHome(MainScreenDestination.Home)
                }, modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}
