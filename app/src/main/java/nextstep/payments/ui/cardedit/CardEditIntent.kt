package nextstep.payments.ui.cardedit

sealed interface CardEditIntent {
    data class FetchCreditCard(val cardId: Long) : CardEditIntent
}