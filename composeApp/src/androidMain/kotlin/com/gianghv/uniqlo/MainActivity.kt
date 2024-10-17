package com.gianghv.uniqlo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.screen.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        var productId: Long? = null
        if (intent?.action == Intent.ACTION_VIEW && intent.data != null) {
            // Parse deep link data from the URL
            val id = intent.data?.getQueryParameter("id")
            productId = id?.toLongOrNull()
        }

        setContent {
            Box(modifier = Modifier.fillMaxSize()) {
                MainView(productId)
            }
        }
    }
}
