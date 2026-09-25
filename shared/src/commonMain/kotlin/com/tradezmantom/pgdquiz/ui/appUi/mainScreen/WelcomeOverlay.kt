package com.tradezmantom.pgdquiz.ui.appUi.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tradezmantom.pgdquiz.ui.appUi.BannerAdPlacement
import com.tradezmantom.pgdquiz.ui.appUi.PlatformBannerAd
import com.tradezmantom.pgdquiz.util.STARTING_LIVES
import org.jetbrains.compose.resources.painterResource
import pgdquiz.shared.generated.resources.Res
import pgdquiz.shared.generated.resources.drainlogo
import pgdquiz.shared.generated.resources.flame1
import pgdquiz.shared.generated.resources.gasslogo
import pgdquiz.shared.generated.resources.plumblogo

/**
 * Shown when the app opens: a quick rundown of lives, streak and the daily reset, with an
 * upgrade prompt for free users. [onClose] receives whether "don't show again" was ticked.
 */
@Composable
fun WelcomeOverlay(
    modifier: Modifier = Modifier,
    dontShowAgain: Boolean,
    onDontShowAgainChange: (Boolean) -> Unit,
    onClose: () -> Unit,
    showUpgrade: Boolean = true,
    showAds: Boolean = true,
    onUpgrade: () -> Unit = {},
) {
    val glowColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = glowColor,
                spotColor = glowColor
            )
            .clip(RoundedCornerShape(16.dp))
            .background(brush = gradientBrush)
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.outline,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.outline
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                text = "Welcome to PGD Quiz!",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                color = Color.White,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 4.dp, start = 32.dp, end = 32.dp)
            )
            Text(
                text = "Here's how it works",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f)
            )

            InfoRow(
                icon = {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        listOf(Res.drawable.drainlogo, Res.drawable.plumblogo, Res.drawable.gasslogo)
                            .forEach { logo ->
                                // The logos are dark, so sit them on a light disc to stand out
                                // against the overlay's dark gradient.
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.9f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(logo),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        contentScale = ContentScale.Fit
                                    )
                                }
                            }
                    }
                },
                title = "Lives",
                body = "Each trade has its own $STARTING_LIVES lives, shown by its icon. " +
                        "A wrong answer costs one life."
            )

            InfoRow(
                icon = {
                    // flame1.png has lots of transparent padding (flame fills ~57% of its height),
                    // so draw it oversized inside a 48dp slot to get a ~46dp visible flame.
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(Res.drawable.flame1),
                            contentDescription = null,
                            modifier = Modifier.requiredSize(80.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                },
                title = "Streak",
                body = "The flame counts correct answers in a row. A wrong answer resets it."
            )

            InfoRow(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                },
                title = "Daily reset",
                body = "Your lives reset every day at midnight, so you can come back tomorrow for a fresh go."
            )

            if (showUpgrade) {
                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "Don't want to wait?\nUpgrade for unlimited lives, no ads & extra features",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = onUpgrade,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                                    )
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                                RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚡ Upgrade",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onDontShowAgainChange(!dontShowAgain) }
            ) {
                Checkbox(
                    checked = dontShowAgain,
                    onCheckedChange = onDontShowAgainChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = Color.White.copy(alpha = 0.7f),
                        checkmarkColor = Color.White
                    )
                )
                Text(
                    text = "Don't show this again",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }

            if (showAds) {
                PlatformBannerAd(
                    placement = BannerAdPlacement.WELCOME,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.White
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: @Composable () -> Unit,
    title: String,
    body: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.08f))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Box(
            modifier = Modifier.width(96.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = body,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
