package com.example.recorpee.compose.regiterForm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.recorpee.ScreenNav
import com.example.recorpee.compose.PeeViewModel
import com.example.recorpee.ui.theme.RecorPeeTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import java.time.LocalDateTime
@Composable
fun RegisterForm(navController: NavController, viewModel: PeeViewModel) {
    val selectedDate by viewModel.selectedDate.collectAsState()

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
    ) {
        val LocalDateTimeSaver = Saver<LocalDateTime, String>(
            save = { it.toString() },
            restore = { LocalDateTime.parse(it) }
        )

        var baseDateTime by rememberSaveable(stateSaver = LocalDateTimeSaver) {
            mutableStateOf(LocalDateTime.now())
        }

        val dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd(E)", Locale.JAPANESE)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.JAPANESE)
        Text(text = baseDateTime.format(dateFormatter))
        Text(text = baseDateTime.format(timeFormatter))

        Row() {
            Text(text = "0000")
            Text(text = "ml")
        }
        Button(
            onClick = {}
        ) {
            Text(text="Save")
        }
        Button(
            onClick = {}
        ) {
            Text(text="Close")
        }

        Button(
            onClick = {navController.navigate(ScreenNav.DataScreen.route)}
        ) {
            Text(text="Go to second")
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

