package nextstep.payments.ui.cardedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import nextstep.payments.repository.PaymentCardsRepository

class CardEditViewModel(
    private val repository: PaymentCardsRepository = PaymentCardsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CardEditUiState>(CardEditUiState.Loading)
    val uiState: StateFlow<CardEditUiState> get() = _uiState.asStateFlow()

    private val _effect = Channel<CardEditEffect>()
    val effect: Flow<CardEditEffect> get() = _effect.receiveAsFlow()

    fun onIntent(intent: CardEditIntent) {
        when (intent) {
            is CardEditIntent.FetchCreditCard -> fetchCreditCard(intent.cardId)
        }
    }

    private fun fetchCreditCard(cardId: Long) {
        viewModelScope.launch {
            val card = repository.findCard(cardId)
            if (card == null) {
                _effect.send(CardEditEffect.ShowError("카드 정보를 찾을 수 없습니다."))
                return@launch
            }
            _uiState.value = CardEditUiState.Success(card)
        }
    }
}