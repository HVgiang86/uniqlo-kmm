package com.gianghv.uniqlo.presentation.screen.onboarding

import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.onboarding_1
import uniqlo.composeapp.generated.resources.onboarding_2
import uniqlo.composeapp.generated.resources.onboarding_3
import uniqlo.composeapp.generated.resources.onboarding_desc_1
import uniqlo.composeapp.generated.resources.onboarding_desc_2
import uniqlo.composeapp.generated.resources.onboarding_desc_3
import uniqlo.composeapp.generated.resources.onboarding_title_1
import uniqlo.composeapp.generated.resources.onboarding_title_2
import uniqlo.composeapp.generated.resources.onboarding_title_3

@Immutable
data class OnBoardingScreenData(
    val title: StringResource,
    val description: StringResource,
    val imageRes: DrawableResource
)

val onBoardingPages: List<OnBoardingScreenData> = listOf(
    OnBoardingScreenData(
        Res.string.onboarding_title_1, Res.string.onboarding_desc_1, Res.drawable.onboarding_1
    ),
    OnBoardingScreenData(
        Res.string.onboarding_title_2, Res.string.onboarding_desc_2, Res.drawable.onboarding_2
    ),
    OnBoardingScreenData(
        Res.string.onboarding_title_3, Res.string.onboarding_desc_3, Res.drawable.onboarding_3
    ),
)
