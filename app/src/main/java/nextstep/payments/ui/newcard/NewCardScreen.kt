package nextstep.payments.ui.newcard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import nextstep.payments.R
import nextstep.payments.model.IssuingBank
import nextstep.payments.ui.components.PaymentCard
import nextstep.payments.ui.theme.PaymentsTheme

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

    NewCardScreen(
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
        onBackClick = onBackClick,
        onSaveClick = viewModel::onSaveClick,
        modifier = modifier,
    )

    if (showBottomSheet) {
        IssuingBankBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            onIssuingBankSelected = viewModel::setIssuingBank,
        )
    }
}

@Composable
fun NewCardScreen(
    cardNumber: String,
    expiredDate: String,
    ownerName: String,
    password: String,
    issuingBank: IssuingBank?,
    snackBarHostState: SnackbarHostState,
    setCardNumber: (String) -> Unit,
    setExpiredDate: (String) -> Unit,
    setOwnerName: (String) -> Unit,
    setPassword: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = { NewCardTopBar(onBackClick = onBackClick, onSaveClick = onSaveClick) },
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            PaymentCard(
                cardNumber = cardNumber,
                expiredDate = expiredDate,
                ownerName = ownerName,
                issuingBank = issuingBank
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = setCardNumber,
                singleLine = true,
                label = { Text(stringResource(R.string.new_card_card_number_label)) },
                placeholder = { Text(stringResource(R.string.new_card_card_number_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = expiredDate,
                onValueChange = setExpiredDate,
                label = { Text(stringResource(R.string.new_card_expired_date_label)) },
                singleLine = true,
                placeholder = { Text(stringResource(R.string.new_card_expired_date_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = ownerName,
                onValueChange = setOwnerName,
                singleLine = true,
                label = { Text(stringResource(R.string.new_card_owner_name_label)) },
                placeholder = { Text(stringResource(R.string.new_card_owner_name_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
            )

            OutlinedTextField(
                value = password,
                onValueChange = setPassword,
                singleLine = true,
                label = { Text(stringResource(R.string.new_card_password_label)) },
                placeholder = { Text(stringResource(R.string.new_card_password_placeholder)) },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
            )
        }
    }
}

@Preview
@Composable
private fun NewCardScreenPreview() {
    PaymentsTheme {
        NewCardScreen(
            cardNumber = "1234 - 5678 - 1234 - 5678",
            expiredDate = "12 / 34",
            ownerName = "홍길동",
            password = "1234",
            issuingBank = IssuingBank.SHINHAN_CARD,
            setCardNumber = {},
            snackBarHostState = SnackbarHostState(),
            setExpiredDate = {},
            setOwnerName = {},
            setPassword = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
