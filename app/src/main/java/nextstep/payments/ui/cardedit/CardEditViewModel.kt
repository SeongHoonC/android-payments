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
import nextstep.payments.model.IssuingBank
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
            is CardEditIntent.OnCardNumberChanged -> changeCardNumber(intent.cardNumber)
            is CardEditIntent.OnExpiredDateChanged -> changeExpireDate(intent.expiredDate)
            is CardEditIntent.OnOwnerNameChanged -> changeOwnerName(intent.ownerName)
            is CardEditIntent.OnPasswordChanged -> changePassword(intent.password)
            is CardEditIntent.OnIssuingBankChanged -> changeIssuingBank(intent.issuingBank)
            is CardEditIntent.OnSaveCardEdit -> saveCardEdit()
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

    private fun changeCardNumber(cardNumber: String) = setStateOnSuccess {
        copy(creditCard = creditCard.copy(cardNumber = cardNumber))
    }

    private fun changeExpireDate(expiredDate: String) = setStateOnSuccess {
        copy(creditCard = creditCard.copy(expiredDate = expiredDate))
    }

    private fun changeOwnerName(ownerName: String) = setStateOnSuccess {
        copy(creditCard = creditCard.copy(ownerName = ownerName))
    }

    private fun changePassword(password: String) = setStateOnSuccess {
        copy(creditCard = creditCard.copy(password = password))
    }

    private fun changeIssuingBank(issuingBank: IssuingBank) = setStateOnSuccess {
        copy(creditCard = creditCard.copy(issuingBank = issuingBank))
    }

    private fun setStateOnSuccess(
        reducer: CardEditUiState.Success.() -> CardEditUiState.Success
    ) {
        val state = _uiState.value as? CardEditUiState.Success ?: return
        _uiState.value = reducer(state)
    }

    private fun saveCardEdit() {
        viewModelScope.launch {
            val success = _uiState.value as? CardEditUiState.Success ?: return@launch
            val lastCard = repository.findCard(success.creditCard.id)
            if (lastCard == null) {
                _effect.send(CardEditEffect.ShowError("수정하려는 카드 정보를 찾을 수 없습니다."))
                return@launch
            }
            if (lastCard == success.creditCard) {
                _effect.send(CardEditEffect.ShowError("변경된 내용이 없습니다."))
                return@launch
            }
            repository.updateCard(success.creditCard)
            _effect.send(CardEditEffect.OnCardEditSaved)
        }
    }
}