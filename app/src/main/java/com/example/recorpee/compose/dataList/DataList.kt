package com.example.recorpee.compose.dataList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recorpee.compose.PeeViewModel
import com.example.recorpee.ui.theme.RecorPeeTheme
import java.time.LocalDate
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toString
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.Instant

@Composable
fun DataList(navController: NavController, viewModel: PeeViewModel) {
    val  baseDate = remember { mutableStateOf(LocalDate.now()) }
    val peeList by viewModel.peeList.collectAsState()
    val totalAmount = remember(peeList) {
        peeList.sumOf { it.amount }
    }
    val totalCount = remember(peeList) {
        peeList.count()
    }

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val coroutineScope = rememberCoroutineScope()
        var baseDateTime by remember { mutableStateOf(LocalDateTime.now()) }
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)", Locale.JAPANESE)
        Text(text = baseDateTime.format(dateFormatter), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = totalAmount.toString() + "ml (" + totalCount.toString() + "回)", fontSize = 46.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // カスタムリスト
        // https://zenn.dev/turtlekazu/articles/4c8a8b586de7d9
        // https://note.com/masato1230/n/n1957dcd3cee0
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9EAF1))
        ) {
            items(peeList) { pee -> // peeList は List<PeeEntity> になっているのでエラーが消える
                Row(
                    modifier = Modifier.padding(start =20.dp,end =20.dp,top =20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val localDateTime = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(pee.date),
                        ZoneId.systemDefault()
                    )
                    val formatter = DateTimeFormatter.ofPattern("HH:mm")
                    Text(localDateTime.format(formatter), fontSize = 24.sp)

                    Spacer(modifier = Modifier.width(20.dp))
                    Text(pee.amount.toString(), fontSize = 24.sp)

                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "✕", // または "X"
                        fontSize = 24.sp,
                        color = Color.Red, // ボタンらしい色味にする場合
                        modifier = Modifier
                            .clickable {
                                coroutineScope.launch {
                                    viewModel.delete(pee)
                                }
                            }
                            .padding(8.dp) // タップしやすいように周囲にタッチ領域（余白）を広げる
                    )
                }
            }
        }


    }
}


@Preview(showBackground = true)
@Composable
fun DataListPreview() {
    RecorPeeTheme {
//        RegisterForm()
    }
}

