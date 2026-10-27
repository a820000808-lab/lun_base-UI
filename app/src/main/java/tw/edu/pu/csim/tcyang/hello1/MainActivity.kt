package tw.edu.pu.csim.tcyang.hello1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import tw.edu.pu.csim.tcyang.hello1.ui.theme.Hello1Theme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("導覽列 (Top Bar)") }
            )
        },
        bottomBar = {
            BottomAppBar {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("結束列 (Bottom Bar)")
                }
            }
        }
    ) { innerPadding ->
        Main(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumnExample()
        ColumnExample()
    }
}

@Composable
fun LazyColumnExample() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Add a single item
        item {
            Text(text = "Header")
        }

        // Add 50 items
        items(50) { index ->
            Text(text = "$index")
        }

        // Add another single item
        item {
            Text(text = "Footer")
        }
    }
}

@Composable
fun ColumnExample(modifier: Modifier = Modifier) {
    // 預設為空字串（不預設顯示任何文字）
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
        Text("行動應用軟體開發：基本元件")
        Text("作者：陳正倫")
        // 手勢觸發時才會顯示對應文字
        if (gestureState.isNotEmpty()) {
            Text(gestureState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Hello1Theme {
        MainScreen()
    }
}