package com.viona.thumbnail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.viona.myapplication.R
import com.viona.thumbnail.ui.theme.MyApplicationTheme

class MainActivity :
    ComponentActivity(),
    OverlayReceiver.OnHardwareKeysPressedListener {
    private var showOverlayView by mutableStateOf(false)
    private lateinit var recentAppBroadCastReceiver: OverlayReceiver
    private val recentAppsIcon = mutableIntStateOf(R.drawable.ic_launcher_foreground)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recentAppBroadCastReceiver = OverlayReceiver(this)
        registerReceiverWithFilter()
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    Greeting(
                        name = if (showOverlayView) "Overlay" else "Android",
                        modifier = Modifier.padding(innerPadding),
                        showOverlayView,
                        recentAppsIcon,
                    )
                }
            }
        }
    }

    override fun onHomePressed() {
        showOverlayView = true
        recentAppsIcon.intValue = R.drawable.ic_overlay
    }

    override fun onRecentAppsPressed() {
        showOverlayView = true
        recentAppsIcon.intValue = R.drawable.ic_overlay
    }

    override fun onTopResumedActivityChanged(isTopResumedActivity: Boolean) {
        super.onTopResumedActivityChanged(isTopResumedActivity)
        if (!isTopResumedActivity) {
            showOverlayView = true
            recentAppsIcon.intValue = R.drawable.ic_overlay
        }
    }

    override fun onResume() {
        super.onResume()
        recentAppsIcon.intValue = R.drawable.ic_launcher_foreground
        showOverlayView = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(recentAppBroadCastReceiver)
    }

    private fun registerReceiverWithFilter() {
        val filter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(recentAppBroadCastReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(recentAppBroadCastReceiver, filter)
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    showOverlayView: Boolean,
    recentAppsIcon: MutableIntState,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(if (!showOverlayView) Color.Yellow else Color.Transparent),
    ) { }
    val iconState by rememberSaveable { recentAppsIcon }
    Image(
        painter = painterResource(id = iconState),
        contentDescription = "Dynamic Icon",
        Modifier.fillMaxSize()
    )
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting(
            "Android",
            showOverlayView = false,
            recentAppsIcon = mutableIntStateOf(R.drawable.ic_launcher_foreground),
        )
    }
}
