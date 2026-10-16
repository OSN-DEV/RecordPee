package com.example.recorpee.compose.regiterForm

// AlertDialogを使うために必要になる場合があります
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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

@OptIn(ExperimentalMaterial3Api::class) // TimePickerなどに必要
@Composable
fun RegisterForm(navController: NavController, viewModel: PeeViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val focusRequester = remember { FocusRequester() }

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
        val amountText = rememberTextFieldState(initialText = "")

        var baseDateTime by rememberSaveable(stateSaver = LocalDateTimeSaver) {
            mutableStateOf(LocalDateTime.now())
        }

        // --- 追加: 時刻ピッカーの表示状態を管理 ---
        var showTimePicker by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        LaunchedEffect(currentData) {
            val nextText = currentData?.amount?.toString() ?: ""
            if (amountText.text.toString() != nextText) {
                amountText.edit {
                    replace(0, length, nextText)
                }
            }
            baseDateTime = LocalDateTime.now()
        }
        val coroutineScope = rememberCoroutineScope()

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)", Locale.JAPANESE)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.JAPANESE)

        Text(text = baseDateTime.format(dateFormatter), fontSize = 24.sp)
        Spacer(modifier = Modifier.height(20.dp))

        // --- 変更: タップで時刻ダイアログを開くように modifier.clickable を付与 ---
        Text(
            text = baseDateTime.format(timeFormatter),
            fontSize = 46.sp,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { showTimePicker = true }
                .padding(horizontal = 12.dp, vertical = 4.dp) // タップ領域を少し広げる
        )
        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            state = amountText,
            modifier = Modifier.focusRequester(focusRequester),
            textStyle = TextStyle(fontSize = 72.sp, textAlign = TextAlign.Center),
            trailingIcon = { Text("ml", style = MaterialTheme.typography.bodyMedium) },
            lineLimits = TextFieldLineLimits.SingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            inputTransformation = InputTransformation.maxLength(4).then(
                InputTransformation {
                    if (!asCharSequence().all { it.isDigit() }) {
                        revertAllChanges()
                    }
                }
            ),
        )

        Spacer(modifier = Modifier.height(40.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE6E2D1)
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
            modifier = Modifier
                .fillMaxWidth()
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

        // --- 追加: TimePickerDialog の実装 ---
        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = baseDateTime.hour,
                initialMinute = baseDateTime.minute,
                is24Hour = true // 24時間表示にする場合（お好みで変更可）
            )

            TimePickerDialog(
                onDismissRequest = { showTimePicker = false },
                onConfirm = {
                    // 選択された時間・分を既存の baseDateTime の日付に反映して更新
                    baseDateTime = baseDateTime
                        .withHour(timePickerState.hour)
                        .withMinute(timePickerState.minute)
                    showTimePicker = false
                }
            ) {
                TimePicker(state = timePickerState)
            }
        }
    }
}


// --- 追加: Material 3 のダイアログをラップする便利コンポーネント ---
@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RegisterFormPreview() {
    RecorPeeTheme {
//        RegisterForm()
    }
}