package com.example.epact.ui.screens.map

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.epact.data.AppData
import com.example.epact.ui.theme.PactAccent
import com.example.epact.ui.theme.PactBlack
import com.example.epact.ui.theme.PactBorder
import com.example.epact.ui.theme.PactCard
import com.example.epact.ui.theme.PactGreen
import com.example.epact.ui.theme.PactGreenSoft
import com.example.epact.ui.theme.PactMuted
import com.example.epact.ui.theme.PactSurfaceAlt
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
    ) {

        // ── MAPA ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize(),
                update = {
                    it.controller.setZoom(16.0)
                    it.controller.setCenter(pactPoint)
                }
            )

            // Badge "PACT · Évora" sobre o mapa
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(14.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(PactAccent)
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "PACT · Évora",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.06.sp
                )
            }
        }

        // ── SEPARAÇÃO ABAIXO DO MAPA ─────────────────────────────────────
        Spacer(Modifier.height(56.dp))
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))

        // ── MORADA ────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            SectionLabel("MORADA")
            Spacer(Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PactGreenSoft),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = PactAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Text(
                    text = pactAddress,
                    fontSize = 14.sp,
                    color = PactText,
                    lineHeight = 20.sp,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // ── DIVISOR ───────────────────────────────────────────────────────
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))

        // ── AÇÕES ─────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            SectionLabel("EXPLORAR")
            Spacer(Modifier.height(14.dp))

            MapActionRow(
                icon = Icons.Default.Map,
                title = "Abrir no Google Maps",
                subtitle = "Navegar até ao PACT",
                onClick = {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=${Uri.encode(pactAddress)}")
                    )
                    context.startActivity(intent)
                }
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 62.dp)
                    .height(0.5.dp)
                    .background(PactBorder)
            )

            MapActionRow(
                icon = Icons.Default.PhotoCamera,
                title = "Ver Street View",
                subtitle = "Vista 360º externa do PACT",
                onClick = {
                    val uri = Uri.parse(
                        "https://www.google.com/maps/@?api=1&map_action=pano&viewpoint=38.54924,-7.9112233"
                    )
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            )
        }

        // ── DIVISOR ───────────────────────────────────────────────────────
        Box(Modifier.fillMaxWidth().height(0.5.dp).background(PactBorder))

        // ── INFO ──────────────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {


        }


    }
}

// ─── Action row ───────────────────────────────────────────────────────────

@Composable
private fun MapActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(PactSurfaceAlt)
                .border(0.5.dp, PactBorder, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PactAccent,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = PactText
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = PactMuted,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.OpenInNew,
            contentDescription = null,
            tint = PactBorder,
            modifier = Modifier.size(16.dp)
        )
    }
}

// ─── Section label ────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        color = PactMuted,
        letterSpacing = 0.08.sp
    )
}

// ─── OSMDroid lifecycle ───────────────────────────────────────────────────

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
                Lifecycle.Event.ON_RESUME  -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE   -> mapView.onPause()
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