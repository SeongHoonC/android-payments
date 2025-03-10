package nextstep.payments.ui.cardedit

import nextstep.payments.model.CreditCard

sealed interface CardEditUiState {
    data object Loading : CardEditUiState
    data class Success(val creditCard: CreditCard) : CardEditUiState
}
