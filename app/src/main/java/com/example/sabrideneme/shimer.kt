import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.GlobalPositionAwareModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.unit.IntSize
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// --- KODUNUZDAKİ ESKİ SHIMMER İLE İLGİLİ HER ŞEYİ SİLİP BUNU YAPIŞTIRIN ---

private const val SHIMMER_ANIMATION_DURATION = 1200 // Hızı daha iyi görmek için biraz yavaşlattım


// Orijinal Renkler
private val DefaultShimmerColor = Color.Magenta.copy(alpha = 0.5f)
private val DefaultShimmerColors = listOf(
    DefaultShimmerColor,
    DefaultShimmerColor.copy(alpha = 0.2f),
    DefaultShimmerColor,
)

/**
 * Modifier.Node tabanlı, performanslı bir ışıltı efekti uygular.
 *
 * @param isLoading Efektin aktif olup olmayacağını belirler. `false` ise modifier hiçbir şey yapmaz.
 * @param colors Işıltı gradyanında kullanılacak renklerin listesi.
 */
fun Modifier.shimmerEffect(
    isLoading: Boolean,
    colors: List<Color> = DefaultShimmerColors, // Çalıştığını gördükten sonra DebugShimmerColors -> DefaultShimmerColors yapın
): Modifier = this.then(ShimmerModifierElement(isLoading, colors))

private data class ShimmerModifierElement(
    val isLoading: Boolean,
    val colors: List<Color>,
) : ModifierNodeElement<ShimmerModifierNode>() {

    override fun create(): ShimmerModifierNode {
        return ShimmerModifierNode(isLoading, colors)
    }

    override fun update(node: ShimmerModifierNode) {
        node.update(isLoading, colors)
    }
}

private class ShimmerModifierNode(
    var isLoading: Boolean,
    var colors: List<Color>,
) : Modifier.Node(), DrawModifierNode, GlobalPositionAwareModifierNode {

    private val offsetX = Animatable(0f)
    private var size = IntSize.Zero
    private var animationJob: Job? = null

    // Parametreler değiştiğinde bu metod çağrılır
    fun update(isLoading: Boolean, colors: List<Color>) {
        val wasLoading = this.isLoading
        this.isLoading = isLoading
        this.colors = colors

        // Yükleme durumu değiştiyse animasyonu başlat/durdur
        if (wasLoading != isLoading) {
            updateAnimationState()
        }
    }

    // Çizim mantığı
    override fun ContentDrawScope.draw() {
        if (isLoading) {
            val brush = Brush.linearGradient(
                colors = colors,
                start = Offset(offsetX.value, 0f),
                // Gradyanın YATAY olmasını ve soldan sağa akmasını garantiler
                end = Offset(offsetX.value + size.width.toFloat(), 0f),
            )
            // Yükleniyorsa, sadece bu parlama efektini çiz
            drawRect(brush = brush)
        } else {
            // Yüklenmiyorsa, normal içeriği çiz
            drawContent()
        }
    }

    // Composable ekrana yerleştiğinde boyutunu almak için
    override fun onGloballyPositioned(coordinates: LayoutCoordinates) {
        size = coordinates.size
    }

    // Modifier ilk kez eklendiğinde
    override fun onAttach() {
        updateAnimationState()
    }

    // Modifier kaldırıldığında
    override fun onDetach() {
        animationJob?.cancel()
    }

    // Animasyonu başlatan veya durduran ana mantık
    private fun updateAnimationState() {
        animationJob?.cancel() // Önceki animasyonu daima iptal et
        if (isLoading) {
            animationJob = coroutineScope.launch {
                // Boyut değiştiğinde animasyonu yeniden başlatmak için `snapshotFlow` kullanıyoruz.
                // Bu, ekran döndürme gibi durumlarda animasyonun bozulmamasını sağlar.
                snapshotFlow { size }
                    .filter { it.width > 0 } // Genişlik 0 ise animasyonu başlatma
                    .collectLatest { currentSize ->
                        val width = currentSize.width.toFloat()
                        // Animasyonun ekranın tamamen dışından başlayıp tamamen dışına çıkmasını sağlıyoruz
                        val startValue = -2 * width
                        val targetValue = 2 * width

                        offsetX.snapTo(startValue) // Animasyonu başlangıç değerine anında ayarla

                        // Animasyonu sonsuz döngüde çalıştır
                        offsetX.animateTo(
                            targetValue = targetValue,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = SHIMMER_ANIMATION_DURATION,
                                    easing = LinearEasing,
                                )
                            )
                        ) {
                            // !!! EN KRİTİK KISIM !!!
                            // Bu blok, animasyonun HER BİR KARESİNDE çalışır.
                            // `invalidateDraw()` çağrısı, Compose'a "Bu modifier'ın görünümü değişti,
                            // lütfen bir sonraki frame'de `draw()` metodunu tekrar çağır" der.
                            // BU SATIR OLMADAN ANİMASYON GÖRSEL OLARAK GÜNCELLENMEZ.
                            invalidateDraw()
                        }
                    }
            }
        }
    }
}



@Preview
@Composable
fun ShimmerExample() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000) // Simulate a network call
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Example usage on a placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .shimmerEffect(isLoading = isLoading)
                .background(Color.LightGray.copy(alpha = 0.3f))
        )
        // Another example
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .shimmerEffect(isLoading = isLoading)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(CircleShape)
                        .shimmerEffect(isLoading = isLoading)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(24.dp)
                        .shimmerEffect(isLoading = isLoading)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect(isLoading = isLoading)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
            }
        }
    }
}