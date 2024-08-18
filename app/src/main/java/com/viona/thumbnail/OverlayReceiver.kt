package com.viona.thumbnail

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class OverlayReceiver(private val listener: OnHardwareKeysPressedListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
            val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
            when (reason) {
                SYSTEM_DIALOG_REASON_HOME_KEY -> listener.onHomePressed()
                SYSTEM_DIALOG_REASON_RECENT_APPS,
                SYSTEM_DIALOG_REASON_RECENT_APPS_XIAOMI,
                -> listener.onRecentAppsPressed()
            }
            listener.onRecentAppsPressed()
        }
    }

    companion object {
        private const val SYSTEM_DIALOG_REASON_KEY = "reason"
        private const val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        private const val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
        private const val SYSTEM_DIALOG_REASON_RECENT_APPS_XIAOMI = "fs_gesture"
    }

    interface OnHardwareKeysPressedListener {
        fun onHomePressed()
        fun onRecentAppsPressed()
    }
}
