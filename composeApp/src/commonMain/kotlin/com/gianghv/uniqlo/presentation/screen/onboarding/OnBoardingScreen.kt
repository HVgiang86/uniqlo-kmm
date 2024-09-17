package com.gianghv.uniqlo.presentation.screen.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.presentation.component.BlackButtonIconEnd
import com.gianghv.uniqlo.theme.Black_28
import com.gianghv.uniqlo.theme.Silver
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier.fillMaxSize(), onNavigateMain: () -> Unit) {
    val appRepository: AppRepository = koinInject()
    val coroutineScope = rememberCoroutineScope()

    coroutineScope.launch {
        appRepository.setFirstRun(false)
    }

    OnBoardingScreenContent(modifier = modifier, onClickNavigateNext = { pageState ->
        if (pageState.currentPage == onBoardingPages.size - 1) {
            onNavigateMain()
        } else {
            coroutineScope.launch {
                pageState.animateScrollToPage(pageState.currentPage + 1)
            }
        }
    }, onNavigateMain = onNavigateMain)
}

@Composable
fun OnBoardingScreenContent(
    modifier: Modifier = Modifier, onClickNavigateNext: (PagerState) -> Unit, onNavigateMain: () -> Unit
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(1500))
    }
    val windowInsetsPadding = WindowInsets.systemBars.asPaddingValues()
    Column(
        modifier = modifier.padding(windowInsetsPadding).fillMaxSize().verticalScroll(scrollState).padding(top = 40.dp, bottom = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { onBoardingPages.size })
        HorizontalPager(state = pagerState, modifier = Modifier) { pageIndex ->
            val onBoardingScreenData = onBoardingPages[pageIndex]
            OnBoardingPager(
                onBoardingScreenData = onBoardingScreenData, modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp)
            )
        }
        val coroutineScope = rememberCoroutineScope()

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.padding(start = 40.dp, end = 40.dp, top = 24.dp).height(64.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = true, enter = fadeIn(), exit = fadeOut()
            ) {

                BlackButtonIconEnd(modifier = Modifier.fillMaxWidth().height(52.dp), onClick = {
                    onClickNavigateNext(pagerState)
                }, text = {
                    Text(text = "Continue", color = Color.White)
                })
            }
        }

        HorizontalPagerIndicator(modifier = Modifier.padding(top = 30.dp), pagerState = pagerState, onClickIndicator = { index ->
            coroutineScope.launch { pagerState.animateScrollToPage(index) }
        })
    }
}

@Composable
private fun OnBoardingPager(
    modifier: Modifier = Modifier,
    onBoardingScreenData: OnBoardingScreenData,
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Bottom
    ) {
        Image(
            painter = painterResource(onBoardingScreenData.imageRes), contentDescription = null, modifier = Modifier.height(300.dp)
        )

        Text(
            modifier = Modifier.padding(top = 50.dp),
            text = stringResource(onBoardingScreenData.title),
            style = MaterialTheme.typography.titleMedium,
            color = Black_28,
            textAlign = TextAlign.Center
        )

        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = stringResource(onBoardingScreenData.description),
            style = MaterialTheme.typography.bodyMedium,
            color = Black_28,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun HorizontalPagerIndicator(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onClickIndicator: (Int) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        repeat(pagerState.pageCount) { iteration ->
            if (pagerState.currentPage == iteration) Box(
                modifier = Modifier.padding(2.dp).size(height = 13.dp, width = 64.dp).clip(RoundedCornerShape(10.dp))
                    .background(color = MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(10.dp))
            )
            else Box(modifier = Modifier.padding(2.dp).size(13.dp).clip(CircleShape).background(color = Silver, shape = CircleShape)
                .clickable { onClickIndicator(iteration) })
        }
    }
}
