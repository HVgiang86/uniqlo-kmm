import androidx.compose.ui.window.ComposeUIViewController
import com.gianghv.uniqlo.rootview.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
