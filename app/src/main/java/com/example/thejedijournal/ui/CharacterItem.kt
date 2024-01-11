package com.example.thejedijournal.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*

@Composable
fun CharacterShimmerView() {
    val gradient = listOf(
        Color.LightGray.copy(alpha = 0.9f), //darker grey (90% opacity)
        Color.LightGray.copy(alpha = 0.3f), //lighter grey (30% opacity)
        Color.LightGray.copy(alpha = 0.9f)
    )

    val transition = rememberInfiniteTransition(label = "") // animate infinite times

    val translateAnimation = transition.animateFloat( //animate the transition
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // duration for the animation
                easing = FastOutLinearInEasing
            )
        ), label = ""
    )
    val brush = Brush.linearGradient(
        colors = gradient,
        start = Offset(200f, 200f),
        end = Offset(
            x = translateAnimation.value,
            y = translateAnimation.value
        )
    )
    CharacterShimmerItem(brush = brush)
}

@Composable
fun CharacterShimmerItem(brush: Brush) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .wrapContentSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Yellow)
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .background(Color.DarkGray)
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier
                    .size(120.dp, 20.dp)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(
                    modifier = Modifier
                        .size(50.dp, 15.dp)
                        .background(brush)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(
                    modifier = Modifier
                        .size(50.dp, 15.dp)
                        .background(brush)
                )
            }
        }
    }


}

@Composable
fun CharacterItem(name: String, birthday: String, eyeColor: String, onItemClick: ()  -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentWidth()
            .wrapContentHeight()
            .clickable {
                onItemClick()
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.Yellow),
    ) {
        Column(
            modifier = Modifier
                .wrapContentWidth()
                .background(Color.Black)
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = name,
                fontSize = 24.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                color = Color.Yellow,
                softWrap = true,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .wrapContentWidth().height(24.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = eyeColor, color = Color.LightGray, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(text = birthday, color = Color.LightGray, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun ShowLoadingView(showLoading: Boolean) {
    Dialog(onDismissRequest = { !showLoading }) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(50.dp)
                .background(color = Color.DarkGray, shape = CircleShape)
        ) {
            CircularProgressIndicator(modifier = Modifier.size(36.dp))
        }
    }
}

@Preview
@Composable
fun CharacterCardItem() {
    CharacterItem(name = "sdasd", birthday = "asdasd", eyeColor = "asdsad") {

    }
}