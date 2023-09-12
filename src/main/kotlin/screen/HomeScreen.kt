package screen

import Utils.Constants
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


@OptIn(DelicateCoroutinesApi::class)
@Composable
fun HomeScreen() {
    var isStarted = mutableStateOf(false)
    var time by remember {
        mutableStateOf("00:00:00")
    }

    var lastTimeStamp = 0L
    var timeMills = 0L

    Scaffold {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(time)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { isStarted.value = true }) {
                    Text("Start")
                }

                Button(onClick = { isStarted.value = false }) {
                    Text("Pause")
                }

                Button(onClick = {
                    isStarted.value = false
                    time = "00:00:00"
                    lastTimeStamp = 0L
                    timeMills = 0L
                }) {
                    Text("Stop")
                }
            }

            if (isStarted.value) {
                GlobalScope.launch {
                    lastTimeStamp = System.currentTimeMillis()

                    while (isStarted.value) {
                        delay(10L)
                        timeMills += System.currentTimeMillis() - lastTimeStamp
                        lastTimeStamp = System.currentTimeMillis()
                        time = formattedTime(timeMills)
                    }


                }
            }
        }
    }
}

fun formattedTime(timeMills: Long): String {
    val localTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMills), ZoneId.systemDefault())
    val formatter = DateTimeFormatter.ofPattern(Constants.timePattern, Locale.getDefault())

    return localTime.format(formatter)
}
