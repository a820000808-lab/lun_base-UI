package tw.edu.pu.csim.tcyang.hello1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tw.edu.pu.csim.tcyang.hello1.ui.theme.Hello1Theme

// 定義動物資料結構
data class Animal(
    val name: String,
    @DrawableRes val imageRes: Int
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
    var selectedAnimal by remember { mutableStateOf("尚未選擇動物") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x8AE0BBE4))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ColumnExample(
            selectedAnimal = selectedAnimal,
            onAnimalSelected = { index, animalName ->
                selectedAnimal = "您點選的是：第 $index 隻動物 - $animalName"
            }
        )
        TextFieldExample()
    }
}

@Composable
fun ColumnExample(
    selectedAnimal: String,
    onAnimalSelected: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var gestureState by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("行動應用軟體開發：基本元件")
        Text("作者：陳正倫")

        if (gestureState.isNotEmpty()) {
            Text(gestureState)
        }

        Spacer(modifier = Modifier.height(8.dp))
        RowExample()

        Spacer(modifier = Modifier.height(16.dp))
        LazyRowExample(onAnimalSelected = onAnimalSelected)

        Spacer(modifier = Modifier.height(12.dp))
        Text(text = selectedAnimal)
    }
}

@Composable
fun RowExample() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.android),
            contentDescription = "Android icon",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Yellow)
                .alpha(0.6f)
        )

        Spacer(modifier = Modifier.width(20.dp))

        Image(
            painter = painterResource(id = R.drawable.compose),
            contentDescription = "Compose icon",
            modifier = Modifier.size(100.dp)
        )
    }
}

@Composable
fun LazyRowExample(
    onAnimalSelected: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val animals = listOf(
        Animal("鴨子", R.drawable.animal0),
        Animal("企鵝", R.drawable.animal1),
        Animal("青蛙", R.drawable.animal2),
        Animal("貓頭鷹", R.drawable.animal3),
        Animal("海豚", R.drawable.animal4),
        Animal("牛", R.drawable.animal5),
        Animal("無尾熊", R.drawable.animal6),
        Animal("獅子", R.drawable.animal7),
        Animal("狐狸", R.drawable.animal8),
        Animal("小雞", R.drawable.animal9)
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 使用 itemsIndexed 來取得每個項目的索引值 (index 從 0 開始)
        itemsIndexed(animals) { index, animal ->
            val displayIndex = index + 1 // 將編號從 1 開始計算 (1, 2, 3, 4, 5...)

            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(180.dp)
                    .clickable {
                        onAnimalSelected(displayIndex, animal.name)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD0BCFF)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = animal.imageRes),
                        contentDescription = animal.name,
                        modifier = Modifier.size(100.dp)
                    )
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        // 顯示編號
                        Text(text = "編號：$displayIndex")
                        Spacer(modifier = Modifier.height(4.dp))
                        // 顯示動物名稱
                        Text(text = animal.name)
                    }
                }
            }
        }
    }
}

@Composable
fun TextFieldExample(modifier: Modifier = Modifier) {
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (inputText.isBlank()) "尚未輸入內容" else "您輸入的是：$inputText"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    Hello1Theme {
        MainScreen()
    }
}