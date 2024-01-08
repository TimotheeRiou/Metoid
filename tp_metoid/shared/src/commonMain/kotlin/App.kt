import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.Metoid.models.WelcomeScreen
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        WelcomeScreen()
    }
}

expect fun getPlatformName(): String