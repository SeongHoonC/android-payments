package nextstep.payments.repository

import nextstep.payments.model.CreditCard

object PaymentCardsRepository {

    private val _cards = mutableListOf<CreditCard>()
    val cards: List<CreditCard> get() = _cards.toList()

    private var nextId = 0L

    fun addCard(card: CreditCard) {
        nextId++
        _cards.add(card.copy(id = nextId))
    }

    fun updateCard(card: CreditCard) {
        val index = _cards.indexOfFirst { it.id == card.id }
        if (index != -1) {
            _cards[index] = card
        }
    }

    fun findCard(cardId: Long): CreditCard? {
        return _cards.find { it.id == cardId }
    }
}
