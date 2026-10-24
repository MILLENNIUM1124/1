package com.leicalux.camera

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.math.hypot
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme()) {
                LeicaLuxAppScreen()
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun LeicaLuxAppScreen() {
    var useHybridStudio by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0B0D))
    ) {
        // Top Mode Switcher Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .background(Color(0xFFE30613), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("L", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("LEICA LUX · 色卡调色盘", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            TextButton(onClick = { useHybridStudio = !useHybridStudio }) {
                Text(
                    if (useHybridStudio) "切换原生Compose调色盘" else "切换完整徕卡工作室",
                    color = Color(0xFFD4AF37),
                    fontSize = 12.sp
                )
            }
        }

        if (useHybridStudio) {
            // Loads the full embedded Leica Camera Studio HTML5 Engine with 8 Leica LUTs, 2D Pad & Watermark Exporter
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.mediaPlaybackRequiresUserGesture = false
                        settings.allowFileAccess = true
                        settings.cacheMode = WebSettings.LOAD_DEFAULT
                        webChromeClient = object : WebChromeClient() {
                            override fun onPermissionRequest(request: PermissionRequest?) {
                                request?.grant(request.resources)
                            }
                        }
                        loadUrl("file:///android_asset/leica_camera_studio.html")
                    }
                }
            )
        } else {
            NativeComposeTonePadDemo()
        }
    }
}

@Composable
fun NativeComposeTonePadDemo() {
    var selectedProfile by remember { mutableStateOf(LeicaColorEngine.profiles.first()) }
    var tone by remember { mutableStateOf(selectedProfile.defaultTone) }
    var color by remember { mutableStateOf(selectedProfile.defaultColor) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "iPhone风格 2D 触控调色盘 (原生 Compose 组件)",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )

        // 2D Interactive XY Tone & Color Pad
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(Color(0xFF141418), RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF2E2E38), RoundedCornerShape(20.dp))
                .pointerInput(Unit) {
                    detectDragGestures { change, _ ->
                        val w = size.width.toFloat().coerceAtLeast(1f)
                        val h = size.height.toFloat().coerceAtLeast(1f)
                        val nx = (change.position.x / w).coerceIn(0f, 1f)
                        val ny = (change.position.y / h).coerceIn(0f, 1f)
                        color = ((nx * 2f - 1f) * 100f).roundToInt().toFloat()
                        tone = ((1f - ny * 2f) * 100f).roundToInt().toFloat()
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val pad = 28.dp.toPx()
                val uw = size.width - pad * 2
                val uh = size.height - pad * 2
                val curX = pad + ((color + 100f) / 200f) * uw
                val curY = pad + ((100f - tone) / 200f) * uh

                val cols = 11
                val rows = 7
                for (r in 0 until rows) {
                    for (c in 0 until cols) {
                        val gx = pad + (c.toFloat() / (cols - 1)) * uw
                        val gy = pad + (r.toFloat() / (rows - 1)) * uh
                        val dist = hypot(gx - curX, gy - curY)
                        val prox = (1f - dist / 220f).coerceIn(0f, 1f)
                        drawCircle(
                            color = Color.White.copy(alpha = 0.25f + prox * 0.65f),
                            radius = (3f + prox * 6f).dp.toPx() * 0.4f,
                            center = Offset(gx, gy)
                        )
                    }
                }

                drawCircle(
                    color = Color(0xFFE30613),
                    radius = 10.dp.toPx(),
                    center = Offset(curX, curY)
                )
            }
        }

        Text(
            "当前坐标: 影调 Tone ${tone.roundToInt()}  ·  色温 Color ${color.roundToInt()}  ·  色卡: ${selectedProfile.name}",
            color = Color(0xFFD4AF37),
            fontSize = 13.sp
        )
    }
}
