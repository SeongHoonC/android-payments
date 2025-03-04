package nextstep.payments.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import nextstep.payments.model.CreditCard
import nextstep.payments.model.IssuingBank
import nextstep.payments.ui.theme.PaymentsTheme
import nextstep.payments.ui.theme.Typography

@Composable
fun PaymentCard(
    creditCard: CreditCard,
    modifier: Modifier = Modifier,
) {
    PaymentCard(
        cardNumber = creditCard.cardNumber,
        expiredDate = creditCard.expiredDate,
        ownerName = creditCard.ownerName,
        issuingBank = creditCard.issuingBank,
        modifier = modifier
    )
}

@Composable
fun PaymentCard(
    cardNumber: String,
    expiredDate: String,
    ownerName: String,
    issuingBank: IssuingBank?,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .shadow(8.dp)
            .size(width = 208.dp, height = 124.dp)
            .background(
                color = issuingBank?.color ?: Color(0xFF333333),
                shape = RoundedCornerShape(5.dp),
            )
            .padding(horizontal = 14.dp, vertical = 16.dp)
    ) {
        Text(
            text = issuingBank?.bankName ?: "",
            color = Color.White,
            style = Typography.titleSmall,
            modifier = Modifier.align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .size(width = 40.dp, height = 26.dp)
                .background(
                    color = Color(0xFFCBBA64),
                    shape = RoundedCornerShape(4.dp),
                )
        )
        PaymentCardContent(
            cardNumber = cardNumber,
            expiredDate = expiredDate,
            ownerName = ownerName,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun PaymentCardContent(
    cardNumber: String,
    expiredDate: String,
    ownerName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = maskCardNumber(cardNumber),
            color = Color.White,
            style = Typography.bodySmall.copy(letterSpacing = 2.sp),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = ownerName,
                color = Color.White,
                style = Typography.bodySmall,
            )
            Text(
                text = maskExpiredDate(expiredDate),
                color = Color.White,
                style = Typography.bodySmall,
            )
        }
    }
}

private fun maskCardNumber(cardNumber: String): String {
    if (cardNumber.length !in 14..16) return cardNumber
    return "${cardNumber.substring(0, 4)} - ${cardNumber.substring(4, 8)} - **** - ****"
}

private fun maskExpiredDate(expiredDate: String): String {
    if (expiredDate.length != 4) return expiredDate
    return "${expiredDate.substring(0, 2)} / ${expiredDate.substring(2, 4)}"
}

@Preview
@Composable
private fun PaymentCardPreview() {
    PaymentsTheme {
        Column {
            PaymentCard(
                CreditCard(
                    cardNumber = "1234567890123456",
                    expiredDate = "1231",
                    ownerName = "홍길동",
                    password = "1234",
                    issuingBank = IssuingBank.HANA_CARD
                )
            )
            Spacer(Modifier.height(20.dp))
            PaymentCard(
                cardNumber = "",
                expiredDate = "",
                ownerName = "",
                issuingBank = null,
            )
        }
    }
}