package nextstep.payments.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nextstep.payments.R
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun PaymentCardAddition(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(5.dp),
        color = Color(0xFFE5E5E5),
        shadowElevation = 8.dp,
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.size(width = 208.dp, height = 124.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "카드 추가 버튼"
            )
        }
    }
}

@Preview
@Composable
private fun CardAdditionPreview() {
    PaymentsTheme {
        PaymentCardAddition(onClick = {})
    }
}
