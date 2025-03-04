package nextstep.payments.ui.newcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nextstep.payments.ui.theme.PaymentsTheme

class NewCardActivity : ComponentActivity() {

    private val viewModel: NewCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaymentsTheme {
                val cardAdded by viewModel.cardAdded.collectAsStateWithLifecycle()

                LaunchedEffect(cardAdded) {
                    if (cardAdded) {
                        navigateToPayments()
                    }
                }
                NewCardScreen(
                    onBackClick = ::finish
                )
            }
        }
    }

    private fun navigateToPayments() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, NewCardActivity::class.java)
        }
    }
}
