package nextstep.payments.ui.newcard

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nextstep.payments.ui.components.IssuingBankBottomSheet
import nextstep.payments.ui.components.PaymentCardFormScreen

@Composable
fun NewCardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewCardViewModel = viewModel(),
) {
    val cardNumber by viewModel.cardNumber.collectAsStateWithLifecycle()
    val expiredDate by viewModel.expiredDate.collectAsStateWithLifecycle()
    val ownerName by viewModel.ownerName.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val issuingBank by viewModel.issuingBank.collectAsStateWithLifecycle()
    var showBottomSheet by remember { mutableStateOf(true) }

    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest {
            when (it) {
                is NewCardEffect.ShowError -> {
                    coroutineScope.launch { snackBarHostState.showSnackbar(it.message) }
                }
            }
        }
    }

    PaymentCardFormScreen(
        cardNumber = cardNumber,
        expiredDate = expiredDate,
        ownerName = ownerName,
        password = password,
        issuingBank = issuingBank,
        snackBarHostState = snackBarHostState,
        setCardNumber = viewModel::setCardNumber,
        setExpiredDate = viewModel::setExpiredDate,
        setOwnerName = viewModel::setOwnerName,
        setPassword = viewModel::setPassword,
        modifier = modifier,
        topBar = {
            NewCardTopBar(
                onBackClick = onBackClick,
                onSaveClick = viewModel::onSaveClick,
            )
        }
    )

    if (showBottomSheet) {
        IssuingBankBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onIssuingBankSelected = viewModel::setIssuingBank,
        )
    }
}
