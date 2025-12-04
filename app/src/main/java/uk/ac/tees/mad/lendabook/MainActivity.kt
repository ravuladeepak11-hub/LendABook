package uk.ac.tees.mad.lendabook

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.lendabook.presentation.navigation.AppNavGraph
import uk.ac.tees.mad.lendabook.ui.theme.LendABookTheme
import uk.ac.tees.mad.lendabook.utils.NotificationHelper
import uk.ac.tees.mad.lendabook.utils.showToast

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        NotificationHelper.createNotificationChannel(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            val isGranted = ContextCompat.checkSelfPermission(this, permission) ==
                    PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                registerForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { granted ->
                    if (!granted) {
                       this.showToast("permission Granted!")
                    }
                }.launch(permission)
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LendABookTheme {
                AppNavGraph()
            }
        }
    }
}
