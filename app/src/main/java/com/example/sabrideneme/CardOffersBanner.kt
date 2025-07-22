package com.example.sabrideneme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.sabrideneme.ui.theme.SabriDenemeTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Custom colors from the design
val CardRed = Color(0xFFC62828)
val CardBlack = Color(0xFF212121)
val CardWhite = Color(0xFFFFFFFF)
val TextOnDark = Color(0xFFFFFFFF)
val TextOnLight = Color(0xFF212121)
val TextLinkColor = Color(0xFFD32F2F) // A slightly different red for the link

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SabriDenemeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                }
            }
        }
    }
}




@Composable
fun CardOffer(
    title: String,
    caption: String,
    linkText: String,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        // Box, elemanları üst üste (Z ekseninde) katmanlamak için kullanılır.
        // Tanımlama sırası, katman sırasını belirler.
        Box(
            modifier = Modifier
                .fillMaxSize()
                // İllüstrasyonun kartın yuvarlak köşelerinden taşmasını engeller.
                .clip(RoundedCornerShape(12.dp))
        ) {
            // 1. İllüstrasyon (en önce çizilir, yani en altta kalır)
            // İsteğiniz üzerine, ikon doğrudan buraya yerleştirildi.
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd) // İkonu sağa yaslar
                    .fillMaxHeight()            // Yüksekliği kart kadar yapar
                    .width(120.dp),             // Genişliği 120.dp olarak ayarlar
                tint = if (backgroundColor == CardRed) TextOnDark else CardRed
            )

            // 2. Metin içeriği
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    // Metnin, 120dp'lik ikonun üzerine gelmemesi için sağdan boşluk bırakılır.
                    .padding(start = 16.dp, top = 16.dp, end = 130.dp)
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Text(
                    text = caption,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // 3. Alt kısımdaki link
            Text(
                text = linkText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline
                ),
                color = if (backgroundColor == CardRed) TextOnDark else TextLinkColor,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, bottom = 16.dp)
            )

            // 4. "Daha fazla" ikonu (en son çizilir, yani en üstte kalır)
            IconButton(
                onClick = { /* TODO: Handle more options click */ },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More Options",
                    tint = contentColor
                )
            }
        }
    }
}


// --- Önizlemeler yeni düzeni gösterecek şekilde aynı kalır ---

@Preview(name = "Card Offer - White")
@Composable
fun CardOfferPreviewWhite() {
    CardOffer(
        title = "TITLE",
        caption = "Caption",
        linkText = "Text Link",
        backgroundColor = CardWhite,
        contentColor = TextOnLight
    )
}

@Preview(name = "Card Offer - Black")
@Composable
fun CardOfferPreviewBlack() {
    CardOffer(
        title = "TITLE",
        caption = "Caption",
        linkText = "Text Link",
        backgroundColor = CardBlack,
        contentColor = TextOnDark
    )
}

@Preview(name = "Card Offer - Red")
@Composable
fun CardOfferPreviewRed() {
    CardOffer(
        title = "TITLE",
        caption = "Caption",
        linkText = "Text Link",
        backgroundColor = CardRed,
        contentColor = TextOnDark
    )
}