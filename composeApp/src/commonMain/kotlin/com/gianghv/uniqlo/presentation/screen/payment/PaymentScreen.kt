package com.gianghv.uniqlo.presentation.screen.payment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gianghv.uniqlo.presentation.component.AppErrorDialog
import com.gianghv.uniqlo.presentation.component.LoadingDialog
import com.gianghv.uniqlo.presentation.screen.main.navigation.MainScreenDestination
import com.gianghv.uniqlo.util.asState
import com.gianghv.uniqlo.util.logging.AppLogger
import com.multiplatform.webview.util.KLogSeverity
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberSaveableWebViewState
import com.multiplatform.webview.web.rememberWebViewNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(viewModel: PaymentViewModel, orderId: Long?, amount: Double?, onBack: () -> Unit, navigateTo: (MainScreenDestination) -> Unit) {
    val state by viewModel.state.asState()

    val webviewState = rememberSaveableWebViewState(url = "https://google.com").apply {
        webSettings.logSeverity = KLogSeverity.Verbose
    }

    var lastUrl by remember { mutableStateOf("") }

    val navigator = rememberWebViewNavigator()

    if (state.isLoading) {
        LoadingDialog()
    }

    if (state.error != null) {
        if (state.paymentStatus == PaymentStatus.FAIL) {
            AppErrorDialog(state.error?.throwable, onDismissRequest = {
                navigateTo(MainScreenDestination.OrderHistory)
            })
        } else {
            AppErrorDialog(state.error?.throwable, onDismissRequest = {})
        }
    }

    LaunchedEffect(Unit) {
        AppLogger.d("Start oayment screen with orderId: $orderId, amount: $amount")
        viewModel.sendEvent(PaymentUiEvent.LoadPaymentUrl(amount = amount?.toLong() ?: 0, orderId = orderId))
    }

    if (webviewState.lastLoadedUrl != null && state.isWebViewOpened) {
        if (lastUrl != webviewState.lastLoadedUrl.toString()) {
            lastUrl = webviewState.lastLoadedUrl.toString()
            viewModel.handleRedirectUrl(webviewState.lastLoadedUrl.toString())
        }
    }

    if (state.cannotCreatePayment != null) {
        AppErrorDialog(state.cannotCreatePayment?.throwable, onDismissRequest = {
            onBack()
        })
    }

    if (state.paymentStatus == PaymentStatus.PAID) {
        viewModel.sendEvent(PaymentUiEvent.UpdateOrderStatus(state.orderId ?: 0))
    }

    if (state.paymentStatus == PaymentStatus.SUCCESS) {
        navigateTo(MainScreenDestination.OrderResult(mapOf(MainScreenDestination.OrderResult.IS_ORDER_SUCCESS_KEY to true)))
    }

    if (state.paymentUrl != null && state.isWebViewOpened) {
        LaunchedEffect(navigator) {
            val bundle = webviewState.viewState
            if (bundle == null) {
                // This is the first time load, so load the page.
                navigator.loadUrl(state.paymentUrl.toString())
            }
        }

        Scaffold(topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
                Text(text = "${webviewState.pageTitle}", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            }, actions = {
                IconButton(onClick = {
                    viewModel.sendEvent(PaymentUiEvent.CloseWebView(webviewState.lastLoadedUrl.toString()))
                }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            })
        }) {
            Box(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())) {
                WebView(webviewState, modifier = Modifier.fillMaxSize(), navigator = navigator)
            }
        }

    } else {
        Scaffold(topBar = {
            TopAppBar(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), title = {
                Text(text = "VNPay Payment", style = MaterialTheme.typography.titleMedium, color = Color.Black)
            }, actions = {
                IconButton(onClick = {

                }) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = null)
                }
            }, navigationIcon = {
                IconButton(onClick = {
                    onBack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }) {
            Box(modifier = Modifier.fillMaxSize().padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())) {
                Column(modifier = Modifier.fillMaxWidth().wrapContentHeight().align(Alignment.Center)) {
                    if (state.paymentUrl != null) {
                        Text(
                            text = "Payment is in progress...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        Text(
                            text = "Creating payment url...",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Black,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}
