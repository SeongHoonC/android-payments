package nextstep.payments.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import nextstep.payments.model.CreditCard
import nextstep.payments.model.IssuingBank
import org.junit.Rule
import org.junit.Test

class PaymentCardTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun 카드_번호가_뒷_8자리는_가려진다() {
        // given
        composeTestRule.setContent {
            PaymentCard(
                creditCard = CreditCard(
                    cardNumber = "1234567812345678",
                    expiredDate = "0101",
                    ownerName = "최성훈",
                    password = "1234",
                    IssuingBank.BC_CARD,
                )
            )
        }

        // then
        composeTestRule.onNodeWithText("1234 - 5678 - **** - ****").assertExists()
        composeTestRule.onNodeWithText("1234 - 5678 - 1234 - 5678").assertDoesNotExist()
    }

    @Test
    fun 만료_일자가_형식에_맞게_표시된다() {
        // given
        composeTestRule.setContent {
            PaymentCard(
                creditCard = CreditCard(
                    cardNumber = "1234567812345678",
                    expiredDate = "0123",
                    ownerName = "John Doe",
                    password = "1234",
                    IssuingBank.HYUNDAE_CARD,
                )
            )
        }

        // then
        composeTestRule.onNodeWithText("01 / 23").assertExists()
    }

    @Test
    fun 카드사_이름이_카드에_표시된다() {
        // given
        composeTestRule.setContent {
            PaymentCard(
                creditCard = CreditCard(
                    cardNumber = "1234567812345678",
                    expiredDate = "1223",
                    ownerName = "홍길동",
                    password = "1234",
                    IssuingBank.SHINHAN_CARD,
                )
            )
        }

        // then
        composeTestRule.onNodeWithText("신한카드").assertExists()
    }
}