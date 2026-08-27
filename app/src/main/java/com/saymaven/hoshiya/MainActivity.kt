package com.saymaven.hoshiya

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import com.saymaven.hoshiya.core.model.TimerState
import com.saymaven.hoshiya.core.theme.HoshiyaTheme
import com.saymaven.hoshiya.ui.navigation.HoshiyaNavHost
import com.saymaven.hoshiya.ui.timer.TimerViewModel

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ ->
        // Notification permission handled
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        setContent {
            val timerViewModel: TimerViewModel = viewModel()
            val uiState by timerViewModel.uiState.collectAsStateWithLifecycle()

            HoshiyaTheme(
                palette = uiState.settings.themePalette,
                dynamicColor = uiState.settings.useDynamicColor
            ) {
                // Keep screen on when timer is running if enabled in settings
                if (uiState.settings.keepScreenOn && uiState.timerState == TimerState.RUNNING) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }

                HoshiyaNavHost(timerViewModel = timerViewModel)
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
