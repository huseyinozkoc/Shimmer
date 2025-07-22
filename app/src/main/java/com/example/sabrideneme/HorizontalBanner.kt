import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HorizontalBanner(
    modifier: Modifier = Modifier,
    title: String? = null,
    caption: String? = null,
    textLink: String? = null,
    leftImage: (@Composable () -> Unit)? = null,
    rightImage: (@Composable () -> Unit)? = null,
    onClose: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textLinkColor: Color = contentColor,
    elevation: Dp = 2.dp
) {
    val bannerShape = RoundedCornerShape(12.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .shadow(elevation = elevation, shape = bannerShape)
            .clip(bannerShape)
            .background(containerColor)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sol Resim Alanı
                if (leftImage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(88.dp)
                    ) {
                        leftImage()
                    }
                }

                // Metin Alanı
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    title?.let {
                        Text(
                            text = it,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = LocalContentColor.current
                        )
                    }
                    caption?.let {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = LocalContentColor.current
                        )
                    }
                    textLink?.let {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = textLinkColor,
                            textDecoration = TextDecoration.Underline,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Sağ Resim Alanı veya Boşluk
                if (rightImage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(88.dp)
                            .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                    ) {
                        rightImage()
                    }
                } else {
                    // Kapatma ikonunun metnin üzerine gelmesini önlemek için sağda boşluk bırakıyoruz.
                    // İkon 24dp, offset ile 12dp dışarı çıkacak, yani 36dp alan yeterli.
                    Spacer(modifier = Modifier.width(36.dp))
                }
            }
        }

        // --- DEĞİŞİKLİK BURADA ---
        // IconButton yerine doğrudan tıklanabilir bir Icon kullanıldı.
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Kapat", // Erişilebilirlik için önemli
            tint = contentColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                // İkonun merkezini tam köşeye getirmek için boyutunun yarısı kadar kaydırıyoruz.
                // Bu, "yapışık" bir görünüm sağlar.
                .padding(4.dp)
                .clickable(
                    role = Role.Button, // Erişilebilirlik: Ekran okuyuculara bunun bir buton olduğunu söyler.
                    onClick = onClose
                )
        )
    }
}



// Önizleme fonksiyonu aynı şekilde çalışmaya devam edecektir.
@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0, name = "Banner with clickable Icon")
@Composable
fun BannerPreviewWithClickableIcon() {
    val dummyImage: @Composable (Color) -> Unit = { tintColor ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "₺", fontSize = 40.sp, color = tintColor)
        }
    }

    val dummyImage2: @Composable (Color) -> Unit = { tintColor ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {

        }
    }

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Örnek 1: Resimli
        HorizontalBanner(
            title = "Title",
            caption = "Caption",
            textLink = "Text Link",
            rightImage = { dummyImage(Color(0xFFD32F2F)) },
            onClose = { },
            containerColor = Color.White,
            contentColor = Color.Black,
            textLinkColor = Color(0xFFD32F2F)
        )

        // Örnek 2: Resimsiz
        HorizontalBanner(
            title = "Title",
            textLink = "Text Link",
            onClose = { },
            containerColor = Color(0xFFD32F2F),
            contentColor = Color.White,
            textLinkColor = Color.White
        )

        // Örnek 2: Resimsiz
        HorizontalBanner(
            title = "Title",
            caption = "Caption",
            textLink = "Text Link",
            leftImage = { dummyImage(Color(0xFFFAF8F8)) },
            onClose = { },
            containerColor = Color(0xFFD32F2F),
            contentColor = Color.White,
            textLinkColor = Color.White
        )

        // Örnek 2: Resimsiz
        HorizontalBanner(
            title = "Title",
            caption = "Caption",
            textLink = "Text Link",
            onClose = { },
            containerColor = Color(0xFF000000),
            contentColor = Color.White,
            textLinkColor = Color.White
        )

        // Örnek 2: Resimsiz
        HorizontalBanner(
            title = "Title",
            textLink = "Text Link",
            onClose = { },
            containerColor = Color(0xFFFFFFFF),
            contentColor = Color.Black,
            textLinkColor = Color.Black
        )
    }
}