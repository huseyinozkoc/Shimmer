package com.example.sabrideneme


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sabrideneme.ui.theme.SabriDenemeTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

/**
 * A single circular dot used by the indicators.
 *
 * @param size The diameter of the dot.
 * @param color The fill color of the dot.
 */
@Composable
private fun IndicatorDot(
    size: Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

/**
 * A horizontal indicator that displays a series of dots.
 * Ideal for indicating the current page in a horizontal pager.
 *
 * @param modifier The modifier to be applied to the indicator row.
 * @param totalItems The total number of dots to display.
 * @param selectedIndex The index of the currently selected dot.
 * @param dotSize The size (diameter) of each dot.
 * @param spacing The spacing between each dot.
 * @param selectedColor The color of the selected dot.
 * @param unselectedColor The color of the unselected dots.
 */
@Composable
fun HorizontalIndicator(
    modifier: Modifier = Modifier,
    totalItems: Int,
    selectedIndex: Int,
    dotSize: Dp,
    spacing: Dp = 8.dp,
    selectedColor: Color,
    unselectedColor: Color
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalItems) { index ->
            val color = if (index == selectedIndex) selectedColor else unselectedColor
            IndicatorDot(
                size = dotSize,
                color = color
            )
        }
    }
}

/**
 * A vertical indicator that displays a series of dots.
 * Ideal for indicating the current step in a vertical stepper.
 *
 * @param modifier The modifier to be applied to the indicator column.
 * @param totalItems The total number of dots to display.
 * @param selectedIndex The index of the currently selected dot.
 * @param dotSize The size (diameter) of each dot.
 * @param spacing The spacing between each dot.
 * @param selectedColor The color of the selected dot.
 * @param unselectedColor The color of the unselected dots.
 */
@Composable
fun VerticalIndicator(
    modifier: Modifier = Modifier,
    totalItems: Int,
    selectedIndex: Int,
    dotSize: Dp,
    spacing: Dp = 8.dp,
    selectedColor: Color,
    unselectedColor: Color
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(totalItems) { index ->
            val color = if (index == selectedIndex) selectedColor else unselectedColor
            IndicatorDot(
                size = dotSize,
                color = color
            )
        }
    }
}







@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndicatorDemoScreen() {
    val totalPages = 5
    var selectedIndex by remember { mutableStateOf(0) }

    // Function to handle next/previous selection, wrapping around
    val onArrowClick: (Int) -> Unit = { direction ->
        selectedIndex = (selectedIndex + direction + totalPages) % totalPages
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Indicator Demo") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // --- Horizontal Indicator Example ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Horizontal Indicator", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

                    HorizontalIndicator(
                        totalItems = totalPages,
                        selectedIndex = selectedIndex,
                        dotSize = 12.dp,
                        spacing = 10.dp,
                        selectedColor = MaterialTheme.colorScheme.primary,
                        unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                }
            }

            // --- Vertical Indicator Example ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalIndicator(
                        totalItems = totalPages,
                        selectedIndex = selectedIndex,
                        dotSize = 16.dp,
                        spacing = 12.dp,
                        selectedColor = Color(0xFFE91E63), // Custom Pink
                        unselectedColor = Color.LightGray
                    )
                    Spacer(Modifier.width(24.dp))
                    Text(
                        "Step ${selectedIndex + 1} of $totalPages",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // --- Controls ---
            Text(
                "Selected Index: $selectedIndex",
                style = MaterialTheme.typography.headlineSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onArrowClick(-1) }) {
                    Text("Previous")
                }
                Button(onClick = { onArrowClick(1) }) {
                    Text("Next")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IndicatorDemoScreenPreview() {
    SabriDenemeTheme() {
        IndicatorDemoScreen()
    }
}