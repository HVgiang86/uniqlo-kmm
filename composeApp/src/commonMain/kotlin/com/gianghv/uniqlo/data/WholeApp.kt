package com.gianghv.uniqlo.data

object WholeApp {
    var USER_ID: Long = 0L
    var RECOMMEND_BASE_URL: String = "https://f7cb-1-55-242-188.ngrok-free.app"
    var CHAT_BASE_URL: String = "https://3ca4-1-55-242-188.ngrok-free.app"
    val priorityProducts = listOf<Long>(
        16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 61, 62, 64, 65, 66, 67, 68, 69, 70, 71, 72, 74, 75, 77, 78
    )

    val testVnPay = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=120000000&vnp_BankCode=NCB&vnp_Command=pay&vnp_CreateDate=20241023061308&vnp_CurrCode=VND&vnp_IpAddr=1.55.242.188%2C+162.158.162.2%2C+10.210.198.94&vnp_Locale=vn&vnp_OrderInfo=Thanh_toan_don_hang&vnp_OrderType=other&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A3003%2Fvnpay%2Freturn%3F%26expire%3D1729664888556&vnp_TmnCode=W5RX0ROL&vnp_TxnRef=23061308&vnp_Version=2.1.0&vnp_SecureHash=8bed081922cb36d2e2bff285cfa52157199b89ddf50d0d8db2d0afda37c6f9e452fb789de41ea241d8464261bae4ad951104f7e989e9bcdad0cfc7b53bd7f7a8"
}
