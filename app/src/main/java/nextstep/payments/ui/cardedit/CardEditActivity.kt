package nextstep.payments.ui.cardedit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

class CardEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val cardId = intent.getLongExtra(KEY_CARD_ID, -1)
        setContent {
            CardEditScreen(
                cardId = cardId,
                onBackClick = ::finish,
                navigateToPayments = ::navigateToPayments
            )
        }
    }

    private fun navigateToPayments() {
        setResult(RESULT_OK)
        finish()
    }

    companion object {
        private const val KEY_CARD_ID = "KEY_CARD_ID"

        fun getIntent(context: Context, cardId: Long): Intent {
            return Intent(context, CardEditActivity::class.java).apply {
                putExtra(KEY_CARD_ID, cardId)
            }
        }
    }
}

