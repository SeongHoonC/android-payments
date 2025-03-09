package nextstep.payments.ui.components

import androidx.compose.runtime.Composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import nextstep.payments.model.IssuingBank
import nextstep.payments.ui.theme.PaymentsTheme
import nextstep.payments.ui.theme.Typography

private const val COLUMN_COUNT = 4

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun IssuingBankBottomSheet(
    onDismissRequest: () -> Unit,
    onIssuingBankSelected: (IssuingBank) -> Unit,
    modifier: Modifier = Modifier
) {
    val modalBottomSheetState = rememberModalBottomSheetState(confirmValueChange = { false })
    val rememberCoroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = modalBottomSheetState,
        onDismissRequest = onDismissRequest,
        dragHandle = {},
        content = {
            NewCardIssuingBankSelection(
                onIssuingBankSelected = { issuingBank ->
                    onIssuingBankSelected(issuingBank)
                    rememberCoroutineScope.launch { modalBottomSheetState.hide() }
                        .invokeOnCompletion { onDismissRequest() }
                },
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(vertical = 36.dp)
            )
        },
        modifier = modifier
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NewCardIssuingBankSelection(
    onIssuingBankSelected: (IssuingBank) -> Unit,
    modifier: Modifier = Modifier
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        maxItemsInEachRow = COLUMN_COUNT,
        modifier = modifier,
    ) {
        IssuingBank.entries.forEach { issuingBank ->
            IssuingBankItem(
                issuingBank = issuingBank,
                onSelected = { onIssuingBankSelected(issuingBank) },
                modifier = Modifier.size(68.dp)
            )
        }
    }
}

@Composable
private fun IssuingBankItem(
    issuingBank: IssuingBank,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = CenterHorizontally,
        modifier = modifier.clickable(onClick = onSelected)
    ) {
        Image(
            painter = painterResource(issuingBank.logo),
            contentDescription = "카드사 아이콘",
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(issuingBank.bankName, style = Typography.bodyMedium)
    }
}

@Preview(name = "카드사 선택", showBackground = true)
@Composable
private fun Preview() {
    PaymentsTheme {
        NewCardIssuingBankSelection(
            onIssuingBankSelected = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IssuingBankItemPreview() {
    PaymentsTheme {
        IssuingBankItem(
            issuingBank = IssuingBank.KB_CARD,
            onSelected = {},
        )
    }
}
