package com.example.recorpee.compose.dataList

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.toString

@Composable
fun DataList(navController: NavController, viewModel: PeeViewModel) {
    val  baseDate = remember { mutableStateOf(LocalDate.now()) }
    val peeList by viewModel.peeList.collectAsState()


    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        Text(text = "2026/10/07(水)")
        Text(text = "11:30")

        // カスタムリスト
        // https://zenn.dev/turtlekazu/articles/4c8a8b586de7d9
        // https://note.com/masato1230/n/n1957dcd3cee0
        LazyColumn {
            items(peeList) { pee -> // peeList は List<PeeEntity> になっているのでエラーが消える
                Row() {
                    Text(pee.date.toString())
                    Text(pee.amount.toString())
                }
            }
        }

        Button(
            onClick = { navController.popBackStack() }
        ) {
            Text(text = "return")
        }
        LazyColumn() {

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

