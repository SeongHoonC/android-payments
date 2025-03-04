package nextstep.payments.ui.payments

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import nextstep.payments.repository.PaymentCardsRepository

class PaymentsViewModel(
    private val repository: PaymentCardsRepository = PaymentCardsRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<PaymentsUiState> = MutableStateFlow(PaymentsUiState.Empty)
    val uiState: StateFlow<PaymentsUiState> = _uiState.asStateFlow()

    fun fetchCards() {
        val cards = repository.cards
        val firstCard = cards.firstOrNull()

        val newState = when {
            firstCard == null -> PaymentsUiState.Empty
            cards.size == 1 -> PaymentsUiState.One(firstCard)
            else -> PaymentsUiState.Many(cards)
        }

        _uiState.update { newState }
    }
}
