package com.example.recorpee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recorpee.compose.PeeViewModel
import com.example.recorpee.ui.theme.RecorPeeTheme
import com.example.recorpee.compose.dataList.DataList
import com.example.recorpee.compose.regiterForm.RegisterForm
import com.example.recorpee.data.AppDatabase
import com.example.recorpee.data.seed
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// コンポーズのナビゲーション
// https://techblog.recochoku.jp/10476


// https://zenn.dev/slowhand/articles/3ab73d3cf95820

sealed class ScreenNav(val route: String) {
    object RootScreen : ScreenNav("RegisterForm")
    object DataScreen: ScreenNav("DataList")
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var appDatabase: AppDatabase
    private val viewModel: PeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ダミーデータ作成
//        appDatabase.seed(lifecycleScope)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            // 1. 現在のバックスタックエントリを取得してルートを監視する
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                    containerColor = androidx.compose.ui.graphics.Color(0xFFF9F9FB),
                // 2. DataScreen の場合のみ FloatingActionButton を配置する
                floatingActionButton = {
                    if (currentRoute == ScreenNav.DataScreen.route) {
                        FloatingActionButton(
                            onClick = {
                                navController.navigate(ScreenNav.RootScreen.route)
                            }
                        ) {
                            // アイコンは適宜好みのものに変更してください（例: 追加アイコン）
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Register"
                            )
                        }
                    }
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = ScreenNav.RootScreen.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(ScreenNav.RootScreen.route) {
                        RegisterForm(navController, viewModel)
                    }
                    composable(ScreenNav.DataScreen.route) {
                        DataList(navController, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecorPeeTheme {
        Greeting("Android")
    }
}
