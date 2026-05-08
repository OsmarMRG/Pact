package com.example.epact

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.epact.ui.app.EpactApp
import com.example.epact.ui.theme.EpactTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = packageName
        setContent {
            EpactTheme {
                EpactApp()
            }
        }
    }
}
