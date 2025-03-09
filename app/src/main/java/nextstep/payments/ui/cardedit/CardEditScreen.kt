package nextstep.payments.ui.cardedit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import nextstep.payments.ui.components.IssuingBankBottomSheet
import nextstep.payments.ui.components.PaymentCardFormScreen
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun CardEditScreen(
    cardId: Long,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(true) }

    val snackBarHostState = remember { SnackbarHostState() }

    when (uiState) {
        is CardEditUiState.Loading -> {
            // Loading
        }

        is CardEditUiState.Success -> {
            val creditCard = (uiState as CardEditUiState.Success).creditCard
            PaymentCardFormScreen(
                cardNumber = creditCard.cardNumber,
                expiredDate = creditCard.expiredDate,
                ownerName = creditCard.ownerName, 
                password = creditCard.password,
                issuingBank = creditCard.issuingBank,
                snackBarHostState = snackBarHostState,
                modifier = modifier,
                topBar = {
                    EditCardTopBar(
                        onSaveClick = {},
                        onBackClick = onBackClick
                    )
                },
                setCardNumber = {},
                setExpiredDate = {},
                setOwnerName = {},
                setPassword = {},
            )
        }
    }

    if (showBottomSheet && uiState is CardEditUiState.Success) {
        IssuingBankBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onIssuingBankSelected = {},
        )
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(CardEditIntent.FetchCreditCard(cardId))
    }
}

@Preview
@Composable
private fun CardEditScreePreview() {
    PaymentsTheme {
        CardEditScreen(
            cardId = -1L,
            onBackClick = {}
        )
    }
}


