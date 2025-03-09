package nextstep.payments.ui.cardedit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import nextstep.payments.R
import nextstep.payments.ui.components.IssuingBankBottomSheet
import nextstep.payments.ui.components.PaymentCardFormScreen

@Composable
fun CardEditScreen(
    cardId: Long,
    onBackClick: () -> Unit,
    navigateToPayments: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CardEditViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(true) }

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is CardEditEffect.ShowError -> snackBarHostState.showSnackbar(it.message)
                is CardEditEffect.OnCardEditSaved -> {
                    navigateToPayments()
                    snackBarHostState.showSnackbar(context.getString(R.string.card_edit_save_card_success))
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(CardEditIntent.FetchCreditCard(cardId))
    }

    when (val state = uiState) {
        is CardEditUiState.Loading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        is CardEditUiState.Success -> {
            val creditCard = state.creditCard
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
                        onSaveClick = { viewModel.onIntent(CardEditIntent.OnSaveCardEdit) },
                        onBackClick = onBackClick
                    )
                },
                setCardNumber = { viewModel.onIntent(CardEditIntent.OnCardNumberChanged(it)) },
                setExpiredDate = { viewModel.onIntent(CardEditIntent.OnExpiredDateChanged(it)) },
                setOwnerName = { viewModel.onIntent(CardEditIntent.OnOwnerNameChanged(it)) },
                setPassword = { viewModel.onIntent(CardEditIntent.OnPasswordChanged(it)) },
            )
        }
    }

    if (showBottomSheet && uiState is CardEditUiState.Success) {
        IssuingBankBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onIssuingBankSelected = { issuingBank ->
                viewModel.onIntent(CardEditIntent.OnIssuingBankChanged(issuingBank = issuingBank))
            },
        )
    }
}

