package com.gianghv.uniqlo.presentation.screen.profile

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.composables.core.rememberDialogState
import com.composables.core.rememberModalBottomSheetState
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.domain.User
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.component.MyAlertDialog
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.presentation.screen.profile.components.ChangePassBottomSheet
import com.gianghv.uniqlo.presentation.screen.profile.components.GoToProductDialog
import com.gianghv.uniqlo.presentation.screen.profile.components.SettingBottomSheet
import com.gianghv.uniqlo.theme.icons.CashStack
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.ext.toCurrencyText
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.rememberAsyncImageState
import com.github.panpf.sketch.request.ComposableImageOptions
import com.github.panpf.sketch.request.placeholder
import kotlinx.coroutines.launch
import uniqlo.composeapp.generated.resources.Res
import uniqlo.composeapp.generated.resources.ic_default_avatar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: ProfileViewModel, navigateTo: (MainScreenDestination) -> Unit, onLogout: () -> Unit) {
    val state by viewModel.state.asState()

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialDetent = Hidden, detents = listOf(Hidden, FullyExpanded)
    )

    val changePassSheetState = rememberModalBottomSheetState(
        initialDetent = Hidden, detents = listOf(Hidden, FullyExpanded)
    )

    val logoutDialogState = remember { mutableStateOf(false) }

    val dialogState = rememberDialogState(initiallyVisible = false)

    LaunchedEffect(Unit) {
        viewModel.sendEvent(ProfileUiEvent.LoadUser)
        viewModel.sendEvent(ProfileUiEvent.LoadOrdersHistory(WholeApp.USER_ID))
    }

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        AppErrorDialog(state.error?.throwable, onDismissRequest = { })
    }

    if (state.isLogout) {
        onLogout()
    }

    if (logoutDialogState.value) {
        MyAlertDialog(title = "Đăng xuất", content = "Bạn xác nhận đăng xuât?", leftBtn = {
            logoutDialogState.value = false
        }, leftBtnTitle = "Huỷ", rightBtn = {
            logoutDialogState.value = false
            viewModel.sendEvent(ProfileUiEvent.Logout)
        }, rightBtnTitle = "Đăng xuất")
    }

    GoToProductDialog(dialogState, onGoTo = {
        dialogState.visible = false
        navigateTo(MainScreenDestination.ProductDetail(mapOf(MainScreenDestination.ProductDetail.PRODUCT_ID_KEY to it)))
    })

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Profile", style = MaterialTheme.typography.titleLarge) })
    }) { innerPadding ->
        var boxWidth by remember { mutableStateOf(0.dp) }
        var boxHeight by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        Box(modifier = Modifier.fillMaxSize().padding(top = innerPadding.calculateTopPadding(), bottom = 80.dp).onGloballyPositioned { layoutCoordinates ->
            val widthInPx = layoutCoordinates.size.width
            boxWidth = with(density) { widthInPx.toDp() }
            val heightInPx = layoutCoordinates.size.height
            boxHeight = with(density) { heightInPx.toDp() }
        }) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                val user = state.user

                if (user != null) {
                    NameCard(modifier = Modifier.padding(16.dp), user = user, onEditClick = {

                    }, screenWidth = boxWidth)
                }

                val orders = state.orders

                if (orders.isNotEmpty()) {
                    Text(
                        "Insight", style = MaterialTheme.typography.titleSmall, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp).padding(
                            top = 16.dp, bottom = 4.dp
                        )
                    )

                    Card(
                        modifier = Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    ) {
                        SettingItem(
                            icon = Icons.Default.ThumbUp,
                            title = "${orders.filter { orderHistory -> orderHistory.status == "accept" }.size} đơn hàng thành công",
                            onClick = {},
                            trailingIcon = false
                        )

                        val totalAmount =
                            orders.filter { orderHistory -> orderHistory.status == "accept" }.sumOf { orderHistory -> (orderHistory.total ?: "0.0").toDouble() }
                                .toCurrencyText()
                        SettingItem(icon = CashStack, title = "$totalAmount đã thanh toán", onClick = {}, trailingIcon = false)
                    }
                }

                Text(
                    "General", style = MaterialTheme.typography.titleSmall, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp).padding(
                        top = 16.dp, bottom = 4.dp
                    )
                )

                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                ) {
                    SettingItem(icon = Icons.Default.Settings, title = "Setting Server", onClick = {
                        scope.launch {
                            bottomSheetState.animateTo(FullyExpanded)
                        }
                    })

                    SettingItem(icon = Icons.Default.Lock, title = "Change Password", onClick = {
                        scope.launch {
                            changePassSheetState.animateTo(FullyExpanded)
                        }
                    })

                    SettingItem(icon = Icons.AutoMirrored.Filled.ExitToApp, title = "Logout", onClick = {
                        logoutDialogState.value = true
                    })
                }

                Text(
                    "More", style = MaterialTheme.typography.titleSmall, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp).padding(
                        top = 16.dp, bottom = 4.dp
                    )
                )

                Card(
                    modifier = Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                ) {
                    SettingItem(icon = Icons.Default.Notifications, title = "Help & Support", onClick = {

                    })

                    SettingItem(icon = Icons.Default.Info, title = "About Us", onClick = {

                    })
                }

            }
        }

        SettingBottomSheet(state = bottomSheetState, onChangeRecommendationServer = {
            viewModel.sendEvent(ProfileUiEvent.ChangeRecommendationServer(it))
        }, onChangeChatServer = {
            viewModel.sendEvent(ProfileUiEvent.ChangeChatServer(it))
        })

        ChangePassBottomSheet(state = changePassSheetState, onChangePassword = { oldPass, newPass ->
            scope.launch {
                changePassSheetState.animateTo(Hidden)
            }
        })
    }
}


@Composable
fun NameCard(modifier: Modifier = Modifier, user: User, onEditClick: () -> Unit, screenWidth: Dp) {
    Card(
        modifier = modifier.wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
    ) {
        Box(modifier = Modifier.padding(16.dp).fillMaxWidth().wrapContentHeight()) {
            Row(modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(end = 40.dp)) {
                Box(modifier = Modifier.width(screenWidth * 0.15f).aspectRatio(1f).border(1.dp, Color.White, CircleShape)) {
                    val isImageLoadedSuccessfully = rememberAsyncImageState(ComposableImageOptions {
                        placeholder(Res.drawable.ic_default_avatar)
                        crossfade()
                    })

                    AsyncImage(
                        uri = user.imagePath,
                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                        state = isImageLoadedSuccessfully,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.CenterVertically)) {
                    Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "#id ${user.id}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            IconButton(onClick = onEditClick, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
        }
    }
}

@Composable
fun SettingItem(modifier: Modifier = Modifier, icon: ImageVector, title: String? = "", onClick: () -> Unit, trailingIcon: Boolean = true) {
    Row(modifier = modifier.padding(8.dp).clickable {
        onClick()
    }, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.wrapContentSize().clip(CircleShape).align(Alignment.CenterVertically)
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.padding(4.dp), tint = Color.Black)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title ?: "",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            maxLines = 1,
            modifier = Modifier.weight(1f).align(Alignment.CenterVertically)
        )

        if (trailingIcon) {
            IconButton(onClick = onClick) {
                Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
            }
        }
    }
}
