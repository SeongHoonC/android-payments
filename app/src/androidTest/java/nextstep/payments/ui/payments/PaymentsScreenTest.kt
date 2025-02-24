package nextstep.payments.ui.payments

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import nextstep.payments.model.CreditCard
import org.junit.Rule
import org.junit.Test

class PaymentsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun 카드_목록이_비어있을_때에는_새로운_카드를_등록해주세요_안내가_노출되어야_한다() {
        // given
        composeTestRule.setContent {
            PaymentsScreen(
                uiState = PaymentsUiState.Empty,
                onAddCardClick = {},
            )
        }

        // then
        composeTestRule.onNodeWithText("새로운 카드를 등록해주세요").assertExists()

        // then - 카드_추가_UI는_목록_하단에_노출된다.
        composeTestRule.onNodeWithContentDescription("카드 추가 버튼").assertExists()
        composeTestRule.onNodeWithText("추가").assertDoesNotExist()
    }

    @Test
    fun 카드_목록에_카드가_한_개_있을_때의_카드_추가_UI는_목록_하단에_노출된다() {
        // given
        val creditCard = CreditCard(
            cardNumber = "1234567812345678",
            expiredDate = "1223",
            ownerName = "홍길동",
            password = "1234"
        )

        composeTestRule.setContent {
            PaymentsScreen(
                uiState = PaymentsUiState.One(creditCard),
                onAddCardClick = {},
            )
        }

        // then
        composeTestRule.onNodeWithText("1234 - 5678 - **** - ****").assertExists()
        composeTestRule.onNodeWithText("홍길동").assertExists()

        // then - 카드_추가_UI는_목록_하단에_노출된다.
        composeTestRule.onNodeWithContentDescription("카드 추가 버튼").assertExists()
        composeTestRule.onNodeWithText("추가").assertDoesNotExist()
    }

    @Test
    fun 카드_목록에_카드가_여러_개_있을_때의_카드_추가_UI는_상단바에_노출된다() {
        // given
        val creditCards = listOf(
            CreditCard(
                cardNumber = "1234567812345678",
                expiredDate = "1223",
                ownerName = "홍길동",
                password = "1234"
            ),
            CreditCard(
                cardNumber = "1111222233334444",
                expiredDate = "1223",
                ownerName = "최성훈",
                password = "1234"
            )
        )

        composeTestRule.setContent {
            PaymentsScreen(
                uiState = PaymentsUiState.Many(creditCards),
                onAddCardClick = {},
            )
        }

        // then
        composeTestRule.onNodeWithText("1234 - 5678 - **** - ****").assertExists()
        composeTestRule.onNodeWithText("홍길동").assertExists()
        composeTestRule.onNodeWithText("1111 - 2222 - **** - ****").assertExists()
        composeTestRule.onNodeWithText("최성훈").assertExists()

        // then - 카드_추가_UI는_상단바에_노출된다.
        composeTestRule.onNodeWithContentDescription("카드 추가 버튼").assertDoesNotExist()
        composeTestRule.onNodeWithText("추가").assertExists()
    }
}