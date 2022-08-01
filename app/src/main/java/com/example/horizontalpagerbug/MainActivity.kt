package com.example.horizontalpagerbug

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.horizontalpagerbug.ui.theme.HorizontalPagerBugTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HorizontalPagerBugTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    val scrollState = rememberPagerState()
                    val scrollButtonState = rememberLazyListState()
                    val density = LocalDensity.current
                    val configuration = LocalConfiguration.current
                    LaunchedEffect(scrollState) {
                        snapshotFlow { scrollState.currentPageOffset }.collectLatest { offsetPercent ->
                            Log.d("ggggg", offsetPercent.toString())
                            val offset = with(density) { (configuration.screenWidthDp.dp * offsetPercent).roundToPx() }
                            scrollButtonState.scrollToItem(scrollState.currentPage, offset)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        HorizontalPagerWidget(scrollState)
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth(),
                            state = scrollButtonState,
                            contentPadding = PaddingValues(vertical = 20.dp)
                        ) {
                            items(2) {
                                Button(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .width(configuration.screenWidthDp.dp)
                                        .padding(horizontal = 20.dp),
                                    onClick = {}
                                ) {
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HorizontalPagerWidget(scrollState: PagerState) {
    HorizontalPager(
        count = 2,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth(),
        state = scrollState
    ) {
        Box(
            modifier = Modifier.size(width = 200.dp, height = 200.dp)
        ) {
            Button(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .background(color = Color.Black)
                    .size(width = 200.dp, height = 200.dp),
                onClick = {}
            ) {
            }
        }
    }
}
