package com.example.sabrideneme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Surface
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    header: @Composable (isExpanded: Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Surface yerine Box kullanıyoruz.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            // Surface'in özelliklerini Modifier'lar ile tek tek ekliyoruz.
            // Modifier'ların sırası önemlidir!

            // 1. Kenarlık (Border): Kenarlığı, arka planın ÜZERİNE çizeriz.
            //    Aynı şekli (shape) burada da belirtmemiz gerekir.
            .border(
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                shape = RoundedCornerShape(12.dp)
            )
            // 2. Arka Plan (Background): Kenarlığın ALTINA arka plan rengini ekleriz.
            //    `shape` parametresi, rengin sadece bu şekil içinde uygulanmasını sağlar.
            .background(
                color = Color.LightGray.copy(alpha = 0.3f),// Genellikle beyaz veya açık gri bir renk
                shape = RoundedCornerShape(12.dp)
            )
            // 3. Kırpma (Clip): İçindeki öğelerin (header, content) kutunun
            //    yuvarlak köşelerinden dışarı taşmasını engellemek için kullanılır.
            //    Bu en güvenli yöntemdir.
            .clip(RoundedCornerShape(12.dp))
    ) {
        Column(
            // Tıklanma efekti (ripple) olmayan clickable modifier
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { isExpanded = !isExpanded }
            )
        ) {
            header(isExpanded)

            AnimatedVisibility(visible = isExpanded) {
                content()
            }
        }
    }
}



// This is the complete screen using our ExpandableCard
@Composable
fun RecentTransactionsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // A light background for the screen
            .padding(16.dp)
    ) {
        ExpandableCard(
            header = { isExpanded -> // Receive the state here
                RecentTransactionsHeader(isExpanded = isExpanded)
            },
            content = {
                RecentTransactionsContent()
            }
        )
    }
}

// Composable for the Header part (Son işlemler...)
@Composable
fun RecentTransactionsHeader(isExpanded: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Son işlemler", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.width(8.dp))
        Badge { Text("2") }
        Spacer(modifier = Modifier.weight(1f)) // Pushes the icon to the end
        Icon(
            // Change icon based on the state
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand"
        )
    }
}

// Composable for the Content part (the list of transactions)
@Composable
fun RecentTransactionsContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp) // Add padding but not at the top
    ) {
        // First transaction
        TransactionItem(
            title = "Para çekme",
            subtitle = "QR MOBIL - 00000",
            amount = "200,00 TL"
        )
        // Divider
        Divider(color = Color.LightGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 8.dp))
        // Second transaction
        TransactionItem(
            title = "Alışveriş/Market",
            subtitle = "MONEYPAY/MIGROSONE",
            amount = "1500,00 TL"
        )
    }
}

// A helper composable for a single transaction row
@Composable
fun TransactionItem(title: String, subtitle: String, amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, fontSize = 14.sp, color = Color.Gray)
        }
        Text(text = amount, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

// Re-implementation of Badge for non-Material 3 users if needed, or just use Material3's
@Composable
fun Badge(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)) {
            content()
        }
    }
}


// --- Preview ---
@Preview(showBackground = true)
@Composable
fun RecentTransactionsScreenPreview() {
    RecentTransactionsScreen()
}