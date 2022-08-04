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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.horizontalpagerbug.ui.theme.HorizontalPagerBugTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Content()
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Content() {
    HorizontalPagerBugTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            val scrollState = rememberPagerState()
            val scrollButtonState = rememberLazyListState()
            val density = LocalDensity.current
            val configuration = LocalConfiguration.current
            LaunchedEffect(scrollState) {
                snapshotFlow { scrollState.currentPageOffset }.collectLatest { offsetPercent ->
                    Log.d("pageOffset", offsetPercent.toString())
                    val offset = with(density) { (configuration.screenWidthDp.dp * offsetPercent).roundToPx() }
                    scrollButtonState.scrollToItem(scrollState.currentPage, offset)
                }
            }
            val horizontalPagerPadding = remember {
                min((configuration.screenWidthDp.dp - 190.dp) / 2, 85.dp)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HorizontalPager(
                    count = 2,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    state = scrollState,
                    contentPadding = PaddingValues(horizontal = horizontalPagerPadding),
                    itemSpacing = 50.dp // FIXME If I comment this everything will be fine
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 200.dp, height = 200.dp)
                            .background(Color.Red)
                    )
                }
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = scrollButtonState,
                    contentPadding = PaddingValues(vertical = 20.dp)
                ) {
                    items(2) {
                        Button(
                            modifier = Modifier
                                .width(configuration.screenWidthDp.dp)
                                .padding(horizontal = 20.dp),
                            onClick = {}
                        ) {
                            Text("Button")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ContentPreview() {
    Content()
}
