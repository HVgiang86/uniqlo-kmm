package com.gianghv.uniqlo.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.font.Font
import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.StateFlow
import platform.Foundation.NSBundle
import platform.Foundation.NSUserDefaults

private val cache: MutableMap<String, Font> = mutableMapOf()



@Composable
actual fun <T> StateFlow<T>.asState(): State<T> = collectAsState()


internal class IosAppVersion : AppVersion {
    override fun code(): String =
        kotlin.runCatching { getInfoDictionary()?.get("CFBundleVersion") as? String ?: "" }
            .getOrDefault("")

    override fun name(): String =
        kotlin.runCatching {
            getInfoDictionary()?.get("CFBundleShortVersionString") as? String ?: ""
        }.getOrDefault("")


    private fun getInfoDictionary() = NSBundle.mainBundle.infoDictionary

}

actual fun isAndroid() = false
actual fun createSettings(): Settings {
    return NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults)
}
