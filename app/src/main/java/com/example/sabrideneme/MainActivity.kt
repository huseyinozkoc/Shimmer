package com.example.sabrideneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.sabrideneme.ui.theme.SabriDenemeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SabriDenemeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // İki örneği de göstermek için ana ekranı düzenleyelim
                    MainScreen()
                }
            }
        }
    }
}

//-------------------------------------------------------------------
// BÖLÜM 1: TEKRAR KULLANILABİLİR SHIMMER ARAÇLARI
//-------------------------------------------------------------------

/**
 * Bir Composable'a koşullu olarak shimmer (parlama) efekti ekleyen bir Modifier.
 *
 * @param isLoading Efektin aktif olup olmayacağını belirler. `false` ise modifier hiçbir şey yapmaz.
 */
fun Modifier.shimmerEffect(isLoading: Boolean = true): Modifier = composed {
    if (!isLoading) {
        return@composed this
    }

    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "ShimmerStartOffsetX"
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}

/**
 * Shimmer efektini yöneten akıllı bir container.
 * İki modda çalışır:
 * 1. `content` null ise, boyutu dışarıdan verilen `modifier` ile belirlenen bir shimmer gösterir (Sabit Boyutlu Mod).
 * 2. `content` dolu ise, `shimmerContent`'ı final `content`'in boyutuna göre boyutlandırır (İçerik Boyutlu Mod).
 */
@Composable
fun ShimmerContainer(
    isLoading: Boolean,
    shimmerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    if (isLoading) {
        if (content != null) {
            // Mod 2: İçerik Boyutlu Shimmer (SubcomposeLayout ile)
            SubcomposeLayout(modifier = modifier) { constraints ->
                val contentPlaceable = subcompose("content", content).first().measure(constraints)
                val shimmerPlaceable = subcompose("shimmer", shimmerContent).first().measure(
                    constraints.copy(
                        minWidth = contentPlaceable.width, maxWidth = contentPlaceable.width,
                        minHeight = contentPlaceable.height, maxHeight = contentPlaceable.height,
                    )
                )
                layout(contentPlaceable.width, contentPlaceable.height) {
                    shimmerPlaceable.placeRelative(0, 0)
                }
            }
        } else {
            // Mod 1: Sabit Boyutlu Shimmer
            shimmerContent()
        }
    } else {
        content?.invoke()
    }
}


//-------------------------------------------------------------------
// BÖLÜM 2: EKRANLAR VE COMPONENT'LER
//-------------------------------------------------------------------

@Composable
fun MainScreen() {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(4000) // Tüm verilerin yüklenmesini 4 saniye simüle et
        isLoading = false
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Örnek 1: Dinamik boyutlu profil kartı
        item {
            Text("Dinamik Boyutlu Kart Örneği", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            ProfileCardExample(isLoading = isLoading)
        }

        // Örnek 2: Sabit boyutlu liste
        item {
            Text("Sabit Boyutlu Liste Örneği", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
        }

        if (isLoading) {
            items(3) {
                // Yüklenirken sabit boyutlu shimmer'lar göster
                ShimmerContainer(
                    isLoading = true,
                    modifier = Modifier.fillMaxWidth().height(80.dp), // Boyutu burada belirliyoruz
                    shimmerContent = { ArticleItemSkeleton() }
                )
                Spacer(Modifier.height(16.dp))
            }
        } else {
            items(5) { index ->
                // Yüklendikten sonra gerçek içerik
                ActualArticleItem(index = index)
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// --- Profil Kartı Örneği için Component'ler ---

data class UserProfile(val name: String, val bio: String)

@Composable
fun ProfileCardExample(isLoading: Boolean) {
    val userProfile = UserProfile(
        name = "Sabri Cihan",
        bio = "Jetpack Compose'u kullanarak harika kullanıcı arayüzleri oluşturmayı seven bir Android geliştirici. Bu açıklama, component'in nasıl uzayabileceğini göstermek için biraz uzun tutulmuştur."
    )

    ShimmerContainer(
        isLoading = isLoading,
        shimmerContent = { ProfileCardSkeleton() },
        content = { ProfileCard(userProfile = userProfile) }
    )
}

@Composable
private fun ProfileCard(userProfile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = userProfile.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(text = userProfile.bio, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun ProfileCardSkeleton() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(64.dp).clip(CircleShape).shimmerEffect())
            Spacer(Modifier.width(16.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(24.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(40.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
            }
        }
    }
}


// --- Makale Listesi Örneği için Component'ler ---

@Composable
fun ArticleItemSkeleton() {
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).shimmerEffect())
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(modifier = Modifier.fillMaxWidth().height(20.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
            Box(modifier = Modifier.fillMaxWidth(0.7f).height(20.dp).clip(RoundedCornerShape(4.dp)).shimmerEffect())
        }
    }
}

@Composable
fun ActualArticleItem(index: Int) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.secondary))
            Spacer(Modifier.width(16.dp))
            Text("Gerçek Makale Başlığı ${index + 1}", style = MaterialTheme.typography.bodyLarge)
        }
    }
}