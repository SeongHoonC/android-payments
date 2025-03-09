package nextstep.payments.ui.cardedit

sealed interface CardEditEffect {
    data class ShowError(val message: String) : CardEditEffect
}

