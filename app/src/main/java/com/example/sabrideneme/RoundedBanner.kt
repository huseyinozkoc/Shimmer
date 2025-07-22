package com.example.sabrideneme

import androidx.compose.foundation.shape.RoundedCornerShape



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RoundedInvestmentCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    linkText: String,
    backgroundColor: Color,
    contentColor: Color,
    iconContainerColor: Color, // Dairesel arkaplanın rengi için yeni parametre
) {
    Card(
        modifier = modifier.height(120.dp),
        // Tasarıma uygun daha yuvarlak köşeler
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
        )
    ) {
        // Card Offers'daki gibi katmanlama için Box kullanıyoruz.
        Box(modifier = Modifier.fillMaxSize()) {

            // 1. Dairesel İkon ve Arkaplanı (En alttaki katman)
            // Bu Box, dairesel arkaplanı oluşturur.
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd) // Sağa hizala
                    .padding(end = 12.dp)       // Kenardan boşluk bırak
                    .size(96.dp)                // Dairenin boyutunu belirle
                    .clip(CircleShape)          // Dairesel olarak kırp
                    .background(iconContainerColor), // Arkaplan rengini uygula
                contentAlignment = Alignment.Center
            ) {
                // Dairenin içindeki asıl ikon
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    tint = contentColor
                )
            }

            // 2. Metin İçeriği (Üstteki katman)
            // Bu Column, tüm metinleri bir arada tutar.
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart) // Sola hizala
                    // Metinlerin dairesel ikonun üzerine gelmemesi için sağdan boşluk bırakılır.
                    // 72dp (ikon) + 12dp (boşluk) = 84dp. Güvenli alan için 90dp kullanalım.
                    .padding(start = 12.dp, end = 90.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = linkText,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (backgroundColor == CardRed) TextOnDark else TextLinkColor
                )
            }
        }
    }
}


// --- Önizlemeler ---

@Preview(name = "Rounded Card - White")
@Composable
fun RoundedCardPreview1() {
    RoundedInvestmentCard(
        title = "TITLE",
        description = "Caption",
        linkText = "Text Link",
        backgroundColor = CardWhite,
        contentColor = TextOnLight,
        iconContainerColor = Color.Gray.copy(alpha = 0.1f)
    )
}

@Preview(name = "Rounded Card - Black")
@Composable
fun RoundedCardPreview2() {
    RoundedInvestmentCard(
        title = "TITLE",
        description = "Caption",
        linkText = "Text Link",
        backgroundColor = CardBlack,
        contentColor = TextOnDark,
        iconContainerColor = Color.White.copy(alpha = 0.1f)
    )
}


@Preview(name = "Rounded Card - Red")
@Composable
fun RoundedCardPreview3() {
    RoundedInvestmentCard(
        title = "TITLE",
        description = "Caption",
        linkText = "Text Link",
        backgroundColor = CardRed,
        contentColor = TextOnDark,
        iconContainerColor = Color.Black.copy(alpha = 0.15f)
    )
}

@Preview(name = "Rounded Card - Investment")
@Composable
fun RoundedCardPreview4() {
    RoundedInvestmentCard(
        title = "ALL INVESTMENTS",
        description = "This is where your investment activity will appear and will change every aspect of your.",
        linkText = "Get Started",
        backgroundColor = CardWhite,
        contentColor = TextOnLight,
        iconContainerColor = Color.Gray.copy(alpha = 0.1f)
    )
}