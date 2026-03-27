package com.example.epact.ui.screens.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.epact.data.AppData
import com.example.epact.ui.components.InfoBlock
import com.example.epact.ui.components.SectionTitle
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactText
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen() {
    val context = LocalContext.current
    val pactAddress = AppData.pactAddress

    // Ajusta estas coordenadas se quiseres afinar o ponto mais tarde
    val pactPoint = GeoPoint(38.5488092, -7.9111854)

    val mapView = rememberMapViewWithLifecycle(
        point = pactPoint,
        title = pactAddress
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PactBlack)
            .padding(16.dp)
    ) {
        SectionTitle("Localização do espaço")
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    BorderStroke(1.dp, PactMuted.copy(alpha = 0.3f)),
                    RoundedCornerShape(28.dp)
                )
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize(),
                update = {
                    it.controller.setZoom(16.0)
                    it.controller.setCenter(pactPoint)
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = PactCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Explorar o espaço",
                    color = PactText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Na aba Media vai encontrar imagens mais atualizadas do espaço e videos institucionais.",
                    color = PactMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoBlock(
            title = "Morada",
            text = pactAddress
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:0,0?q=${Uri.encode(pactAddress)}")
                )
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir no Google Maps")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val streetViewUri = Uri.parse(
                    "https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=38.54924,-7.9112233"
                )
                val intent = Intent(Intent.ACTION_VIEW, streetViewUri)
                context.startActivity(intent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver Street View")
        }

        Spacer(modifier = Modifier.height(8.dp))


    }
}

@Composable
fun rememberMapViewWithLifecycle(
    point: GeoPoint,
    title: String
): MapView {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        Configuration.getInstance().userAgentValue = context.packageName

        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(16.0)
            controller.setCenter(point)

            val marker = Marker(this)
            marker.position = point
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            marker.title = title
            overlays.add(marker)
        }
    }

    DisposableEffect(lifecycle, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    return mapView
}