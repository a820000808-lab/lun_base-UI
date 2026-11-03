package tw.edu.pu.csim.tcyang.hello1

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import tw.edu.pu.csim.tcyang.hello1.ui.theme.Hello1Theme

data class Animal(
    val name: String,
    @DrawableRes val imageRes: Int,
    @RawRes val videoRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Hello1Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Main()
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    var currentVideoRes by remember { mutableStateOf(R.raw.love) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // 1. 原有的半透明背景、手勢、作者、圖示、動物卡片
        ColumnExample()

        // 2. 原有的底部輸入框元件
        TextFieldExample(modifier)

        // 3. 上方的滿版影片播放器與切換影片按鈕
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 滿版影片播放器（內含進度條與暫停提示）
            SimpleVideoView(videoRes = currentVideoRes)

            Spacer(modifier = Modifier.height(4.dp))

            // 保持原本的單一切換影片按鈕
            Button(
                onClick = {
                    currentVideoRes = if (currentVideoRes == R.raw.love) {
                        R.raw.handpan
                    } else {
                        R.raw.love
                    }
                },
                modifier = Modifier.height(36.dp)
            ) {
                val btnText = if (currentVideoRes == R.raw.love) "切換至手碟影片" else "切換至Love影片"
                Text(btnText)
            }
        }
    }
}

@Composable
fun SimpleVideoView(@RawRes videoRes: Int) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    var progress by remember { mutableFloatStateOf(0f) }
    var videoViewRef by remember { mutableStateOf<VideoView?>(null) }

    // 定期更新影片進度條
    LaunchedEffect(isPlaying, videoViewRef) {
        while (true) {
            videoViewRef?.let { view ->
                val duration = view.duration
                val current = view.currentPosition
                if (duration > 0) {
                    progress = current.toFloat() / duration.toFloat()
                }
            }
            delay(200L)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        AndroidView(
            factory = { ctx ->
                VideoView(ctx).apply {
                    val uri = Uri.parse("android.resource://${ctx.packageName}/$videoRes")
                    setVideoURI(uri)
                    setOnPreparedListener { mp ->
                        mp.isLooping = true
                        start()
                    }
                    videoViewRef = this
                }
            },
            update = { view ->
                val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")
                view.setVideoURI(uri)
                view.start()
                isPlaying = true
                videoViewRef = view
            },
            modifier = Modifier.fillMaxSize()
        )

        // 點擊影片即可切換 播放/暫停
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    videoViewRef?.let { view ->
                        if (isPlaying) {
                            view.pause()
                            isPlaying = false
                        } else {
                            view.start()
                            isPlaying = true
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // 當影片暫停時顯示暫停圖示提示
            if (!isPlaying) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "▶",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }
        }

        // 底部影片進度條
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.BottomCenter),
            color = Color.Red,
            trackColor = Color.White.copy(alpha = 0.5f)
        )
    }
}

@Composable
fun ColumnExample(modifier: Modifier = Modifier) {
    var gestureState by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x8AE0BBE4))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { gestureState = "短按" },
                    onDoubleTap = { gestureState = "連按兩下" },
                    onLongPress = { gestureState = "長按" }
                )
            },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(240.dp))

        Text("行動應用軟體開發：基本元件")
        Text("作者：陳正倫")

        if (gestureState.isNotEmpty()) {
            Text(gestureState)
        }

        Spacer(modifier = Modifier.height(4.dp))
        RowExample()

        Spacer(modifier = Modifier.height(4.dp))
        LazyRowExample()
    }
}

@Composable
fun RowExample() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.android),
            contentDescription = "Android icon",
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .background(Color.Yellow)
                .alpha(0.6f)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Image(
            painter = painterResource(id = R.drawable.compose),
            contentDescription = "Compose icon",
            modifier = Modifier.size(70.dp)
        )
    }
}

@Composable
fun LazyRowExample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val animals = listOf(
        Animal("鴨子", R.drawable.animal0, R.raw.love),
        Animal("企鵝", R.drawable.animal1, R.raw.handpan),
        Animal("青蛙", R.drawable.animal2, R.raw.love),
        Animal("貓頭鷹", R.drawable.animal3, R.raw.handpan),
        Animal("海豚", R.drawable.animal4, R.raw.love),
        Animal("牛", R.drawable.animal5, R.raw.handpan),
        Animal("無尾熊", R.drawable.animal6, R.raw.love),
        Animal("獅子", R.drawable.animal7, R.raw.handpan),
        Animal("狐狸", R.drawable.animal8, R.raw.love),
        Animal("小雞", R.drawable.animal9, R.raw.handpan)
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(animals) { index, animal ->
            Card(
                modifier = Modifier
                    .width(160.dp)
                    .height(160.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD0BCFF)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = animal.imageRes),
                        contentDescription = animal.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                Toast.makeText(
                                    context,
                                    "$index.${animal.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "$index.${animal.name}")
                }
            }
        }
    }
}

@Composable
fun TextFieldExample(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            label = { Text("請輸入文字") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = if (inputText.isBlank()) "尚未輸入內容" else "您輸入的是：$inputText"
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Hello1Theme {
        MainScreen()
    }
}