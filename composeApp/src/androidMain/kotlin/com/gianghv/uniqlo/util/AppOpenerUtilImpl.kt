package com.gianghv.uniqlo.util

import android.content.Context

class AppOpenerUtilImpl(private val context: Context) : AppOpenerUtil {
    override fun openStorePage() {/* no-op */
    }

    override fun shareApp() {/* no-op */
    }

    override fun openFeedbackMail() {/* no-op */
    }

    private fun getPlayStoreLink(): String {/* no-op */
        return ""
    }
}
