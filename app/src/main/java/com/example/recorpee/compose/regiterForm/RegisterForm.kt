package com.example.recorpee.compose.regiterForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.recorpee.ScreenNav
import com.example.recorpee.compose.PeeViewModel
import com.example.recorpee.ui.theme.RecorPeeTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RegisterForm(navController: NavController, viewModel: PeeViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 40.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val LocalDateTimeSaver = Saver<LocalDateTime, String>(
            save = { it.toString() },
            restore = { LocalDateTime.parse(it) }
        )
        val currentData by viewModel.currentData.collectAsState()
        val amountText = rememberTextFieldState(
            initialText = viewModel.currentData.value?.amount?.toString() ?: "0"
        )

        var baseDateTime by rememberSaveable(stateSaver = LocalDateTimeSaver) {
            mutableStateOf(LocalDateTime.now())
        }
        val coroutineScope = rememberCoroutineScope()

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)", Locale.JAPANESE)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.JAPANESE)
        Text(text = baseDateTime.format(dateFormatter), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        Text(text = baseDateTime.format(timeFormatter), fontSize = 46.sp)
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            state = amountText,
            textStyle = TextStyle(fontSize = 72.sp, textAlign = TextAlign.Center),
            trailingIcon = { Text("ml", style = MaterialTheme.typography.bodyMedium) },
            lineLimits = TextFieldLineLimits.SingleLine,
//            inputTransformation = InputTransformation.maxLength(4),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = InputTransformation.maxLength(4).then(
                InputTransformation {
                    // this が TextFieldBuffer になるため、直接メソッドを呼べます
                    if (!asCharSequence().all { it.isDigit() }) {
                        revertAllChanges()
                    }
                }
            ),
        )

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6E2D1) // 背景色 (Material 3は containerColor)
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                coroutineScope.launch {
                    viewModel.save(baseDateTime, amountText.text.toString().toInt())
                    viewModel.changeCurrentDate(null)
                    navController.navigate(ScreenNav.DataScreen.route)
                }
            }
        ) {
            Text(text = "Save", color = Color(0xFF292C3D))
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF406272)
            ),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                viewModel.changeCurrentDate(null)
                navController.navigate(ScreenNav.DataScreen.route)
            }
        ) {
            Text(text = "Close", color = Color(0xFFEBE7E0))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    RecorPeeTheme {
//        RegisterForm()
    }
}

