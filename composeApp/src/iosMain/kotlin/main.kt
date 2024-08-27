import androidx.compose.ui.window.ComposeUIViewController
import com.gianghv.uniqlo.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
