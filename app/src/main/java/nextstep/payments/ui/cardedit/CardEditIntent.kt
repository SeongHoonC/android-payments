package nextstep.payments.ui.cardedit

import nextstep.payments.model.IssuingBank

sealed interface CardEditIntent {
    data class FetchCreditCard(val cardId: Long) : CardEditIntent
    data class OnCardNumberChanged(val cardNumber: String) : CardEditIntent
    data class OnExpiredDateChanged(val expiredDate: String) : CardEditIntent
    data class OnOwnerNameChanged(val ownerName: String) : CardEditIntent
    data class OnPasswordChanged(val password: String) : CardEditIntent
    data class OnIssuingBankChanged(val issuingBank: IssuingBank) : CardEditIntent
    data object OnSaveCardEdit : CardEditIntent
}
