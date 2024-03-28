import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.CanvasBasedWindow
import com.mobnetic.newtonstimer.startDi
import com.mobnetic.newtonstimer.timer.NewtonsTimerScreen
import com.mobnetic.newtonstimer.timer.NewtonsTimerViewModel
import com.mobnetic.newtonstimer.ui.theme.MyTheme
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startDi()
    onWasmReady {
        CanvasBasedWindow("Newton's Timer") {

            val timerViewModel = remember { NewtonsTimerViewModel() }

            MyTheme(darkMode = timerViewModel.darkMode) {
                val backgroundColor by animateColorAsState(MaterialTheme.colors.background)
                val outerBackgroundColor = MaterialTheme.colors.onBackground.copy(alpha = 0.04f).compositeOver(backgroundColor)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = outerBackgroundColor)
                ) {
                    AppFrame(backgroundColor = backgroundColor) {
                        NewtonsTimerScreen(timerViewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.AppFrame(
    backgroundColor: Color,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(top = 40.dp, bottom = 64.dp)
            .aspectRatio(9f / 18),
        color = backgroundColor,
        elevation = 32.dp,
        shape = RoundedCornerShape(size = 32.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.onBackground,
        ),
        content = content
    )
}