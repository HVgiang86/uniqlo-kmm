package com.gianghv.uniqlo.presentation.screen.order

import com.gianghv.uniqlo.base.BaseViewModel
import com.gianghv.uniqlo.base.ErrorState
import com.gianghv.uniqlo.base.Reducer
import com.gianghv.uniqlo.base.uiStateHolderScope
import com.gianghv.uniqlo.data.AppRepository
import com.gianghv.uniqlo.data.CartRepository
import com.gianghv.uniqlo.data.UserRepository
import com.gianghv.uniqlo.data.WholeApp
import com.gianghv.uniqlo.data.source.remote.request.CartItemOrderRequest
import com.gianghv.uniqlo.data.source.remote.request.CreateOrderRequest
import com.gianghv.uniqlo.domain.CartItem
import com.gianghv.uniqlo.util.logging.AppLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class OrderViewModel(private val userRepository: UserRepository, private val appRepository: AppRepository, private val cartRepository: CartRepository) :
    BaseViewModel<OrderUiState, OrderUiEvent>() {
    override val state: StateFlow<OrderUiState>
        get() = reducer.state
    override val reducer: Reducer<OrderUiState, OrderUiEvent>
        get() = OrderReducer.getInstance(OrderUiState.initial(), this)

    override fun onClearLoadingState() {
    }

    override fun onClearErrorState() {
    }

    override val onException: (Throwable) -> Unit
        get() = {
            reducer.sendEvent(OrderUiEvent.Error(it))
        }

    fun loadUserDetail(userId: Long) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            userRepository.getMyProfile(userId).collect {
                reducer.sendEvent(OrderUiEvent.LoadUserDetailSuccess(it))
            }
        }
    }

    fun saveOrderInfo(phone: String?, address: String?, email: String?, paymentMethod: PaymentMethodBase?) {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            val saveOrderInfoJob = async {
                appRepository.setSavedOrderInfo(userId = WholeApp.USER_ID, address = address, email = email, phone = phone, paymentMethod = paymentMethod)
            }
            saveOrderInfoJob.await()
        }
    }

    fun loadSavedOrderInfo() {
        uiStateHolderScope(Dispatchers.IO).launch(exceptionHandler) {
            val savedOrderInfoAsync = async {
                appRepository.getSavedOrderInfo()
            }

            val savedOrderInfo = savedOrderInfoAsync.await()
            val addressStr = savedOrderInfo?.address
            val phoneStr = savedOrderInfo?.phone
            val emailStr = savedOrderInfo?.email
            val paymentMethodValue = savedOrderInfo?.paymentMethod?.let { PaymentMethodBase.fromName(it) }

            AppLogger.d("loadSavedOrderInfo: $savedOrderInfo")
            reducer.sendEvent(
                OrderUiEvent.LoadSavedOrderInfoSuccess(
                    address = addressStr, phone = phoneStr, email = emailStr, paymentMethod = paymentMethodValue ?: PaymentMethod.Cash
                )
            )
        }
    }

    fun createOrder(phone: String?, address: String?, paymentMethod: PaymentMethodBase?, orderName: String, carts: List<CartItem>) {
        val orderExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            reducer.sendEvent(OrderUiEvent.CreateOrderFail(throwable))
        }

        val totalPrice = (carts.sumOf { (it.variation?.price ?: 0.0) * (it.quantity ?: 1) } + 50.0 + 30.0) * 1000

        uiStateHolderScope(Dispatchers.IO).launch(orderExceptionHandler) {
            val createOrderRequest = CreateOrderRequest(userId = WholeApp.USER_ID,
                name = orderName,
                address = address ?: "",
                phone = phone ?: "",
                total = totalPrice.toLong(),
                pay = paymentMethod == PaymentMethod.VNPay,
                note = "",
                cartItems = carts.map {
                    CartItemOrderRequest(id = it.id, quantity = it.quantity ?: 1, size = it.size ?: "M", variationId = it.variationId ?: -1L)
                })

            cartRepository.createOrder(createOrderRequest).collect {
                reducer.sendEvent(OrderUiEvent.CreateOrderSuccess(it.id, it.total?.toDoubleOrNull() ?: 0.0))
            }
        }
    }

    fun generateOrderName(): String {
        val currentMoment = Clock.System.now()
        val currentDateTime: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
        val time = currentDateTime.toString("dd/MM/yyyy HH:mm")
        return "Order-$time"
    }

    fun LocalDateTime.toString(format: String): String {
        return this.toString().replace("T", " ").substring(0, 16).replace("-", "/")
    }
}

class OrderReducer(initialVal: OrderUiState, private val viewModel: OrderViewModel) : Reducer<OrderUiState, OrderUiEvent>(initialVal) {
    override fun reduce(oldState: OrderUiState, event: OrderUiEvent) {
        when (event) {
            is OrderUiEvent.Error -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            OrderUiEvent.LoadSavedOrderInfo -> {
                setState(
                    oldState.copy(
                        isLoading = true, error = null, address = null, phone = null, email = null, paymentMethod = null, isHistoryLoaded = false
                    )
                )
                viewModel.loadSavedOrderInfo()
            }

            is OrderUiEvent.LoadSavedOrderInfoSuccess -> {
                val address = event.address ?: oldState.address
                val phone = event.phone ?: oldState.phone
                val email = event.email ?: oldState.email
                val paymentMethod = event.paymentMethod ?: oldState.paymentMethod

                setState(
                    oldState.copy(
                        isLoading = false, error = null, address = address, phone = phone, email = email, paymentMethod = paymentMethod, isHistoryLoaded = true
                    )
                )
            }

            is OrderUiEvent.LoadUserDetail -> {
                setState(oldState.copy(isLoading = true, error = null, user = null))
                viewModel.loadUserDetail(event.userId)
            }

            is OrderUiEvent.LoadUserDetailSuccess -> {
                if (oldState.isHistoryLoaded) {
                    val phone = event.user.phone ?: oldState.phone
                    val email = event.user.email

                    setState(oldState.copy(isLoading = false, error = null, user = event.user, phone = phone, email = email))
                } else {
                    setState(oldState.copy(isLoading = false, error = null, user = event.user))
                }
            }

            is OrderUiEvent.SaveCartList -> {
                setState(
                    oldState.copy(
                        isLoading = true,
                        error = null,
                        carts = event.carts,
                        orderName = viewModel.generateOrderName(),
                        createOrderResult = CreateOrderResult.NOT_YET,
                        total = null,
                        orderId = null
                    )
                )
            }

            is OrderUiEvent.ChangeOrderInfo -> {
                setState(
                    oldState.copy(
                        address = event.address, email = event.email, phone = event.phone, paymentMethod = event.paymentMethod, orderName = event.orderName
                    )
                )
            }

            is OrderUiEvent.SaveOrderInfo -> {
                viewModel.saveOrderInfo(phone = event.phone, address = event.address, email = event.email, paymentMethod = event.paymentMethod)
            }

            is OrderUiEvent.CreateOrder -> {
                setState(oldState.copy(isLoading = true, error = null, createOrderResult = CreateOrderResult.NOT_YET))
                viewModel.createOrder(
                    phone = oldState.phone,
                    address = oldState.address,
                    paymentMethod = oldState.paymentMethod,
                    orderName = oldState.orderName ?: "",
                    carts = oldState.carts
                )
            }

            is OrderUiEvent.CreateOrderFail -> {
                setState(oldState.copy(isLoading = false, error = ErrorState(event.error, true)))
            }

            is OrderUiEvent.CreateOrderSuccess -> {
                if (oldState.paymentMethod == PaymentMethod.Cash) {
                    setState(oldState.copy(isLoading = false, error = null, createOrderResult = CreateOrderResult.CASH))
                } else {
                    setState(
                        oldState.copy(
                            isLoading = false, error = null, createOrderResult = CreateOrderResult.VNPAY, orderId = event.orderId, total = event.total
                        )
                    )
                }

            }
        }
    }

    companion object {
        private var INSTANCE: OrderReducer? = null
        fun getInstance(initialVal: OrderUiState, viewModel: OrderViewModel): OrderReducer {
            if (INSTANCE == null) {
                INSTANCE = OrderReducer(initialVal, viewModel)
            }
            return INSTANCE!!
        }
    }
}
