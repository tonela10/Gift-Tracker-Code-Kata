package com.sedilant.gifttracker.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sedilant.gifttracker.R
import com.sedilant.gifttracker.ui.giftList.GiftListScreen
import com.sedilant.gifttracker.ui.summary.SummaryScreen
import com.sedilant.gifttracker.ui.theme.BackgroundLight
import com.sedilant.gifttracker.ui.theme.GiftTrackerTheme
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onOpenDetails: (Long?) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 2 })

    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(
                visible = pagerState.currentPage == 0,
                enter = slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = spring(Spring.DampingRatioMediumBouncy)
                ),
                exit = slideOutHorizontally(targetOffsetX = { it * 2 })
            ) {
                FloatingActionButton(
                    onClick = { onOpenDetails(null) },
                    containerColor = Color(0xFFB23A48)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_gift),
                        tint = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(padding)
        ) {
            // Tabs row
            TopTabs(pagerState = pagerState)

            // Animated indicator that follows the pager scroll
            val configuration = LocalConfiguration.current
            val screenWidthDp = configuration.screenWidthDp.dp

            // Calculate offset based on pager scroll position
            val indicatorOffset =
                (screenWidthDp / 2) * (pagerState.currentPage + pagerState.currentPageOffsetFraction)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(Color(0xFFEEEEEE))
            ) {
                Box(
                    modifier = Modifier
                        .width(screenWidthDp / 2)
                        .height(3.dp)
                        .offset(x = indicatorOffset)
                        .padding(horizontal = 4.dp)
                        .clip(shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .background(Color(0xFFB23A48))
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                beyondViewportPageCount = 1
            ) { page ->
                when (page) {
                    0 -> GiftListScreen(onOpenDetails = onOpenDetails)
                    1 -> SummaryScreen()
                }
            }
        }
    }
}

@Composable
private fun TopTabs(
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        Tab(
            selected = pagerState.currentPage == 0,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.gift_tracker_tab),
                style = MaterialTheme.typography.titleMedium,
                color = if (pagerState.currentPage == 0) Color(0xFFB23A48) else Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Tab(
            selected = pagerState.currentPage == 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(R.string.summary_tab),
                style = MaterialTheme.typography.titleMedium,
                color = if (pagerState.currentPage == 1) Color(0xFFB23A48) else Color.Gray,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    GiftTrackerTheme {
        HomeScreen(onOpenDetails = {})
    }
}