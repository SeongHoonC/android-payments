package nextstep.payments.ui.payments

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import nextstep.payments.R
import nextstep.payments.model.CreditCard
import nextstep.payments.model.IssuingBank
import nextstep.payments.ui.cardedit.CardEditActivity
import nextstep.payments.ui.components.PaymentCard
import nextstep.payments.ui.components.PaymentCardAddition
import nextstep.payments.ui.newcard.NewCardActivity
import nextstep.payments.ui.theme.PaymentsTheme

@Composable
fun PaymentsScreen(
    modifier: Modifier = Modifier,
    viewModel: PaymentsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val newCardLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.fetchCards()
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.payments_add_card_success))
            }
        }
    }

    val cardEditLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            viewModel.fetchCards()
            coroutineScope.launch {
                snackbarHostState.showSnackbar(context.getString(R.string.payments_edit_card_success))
            }
        }
    }

    val onAddCardClick = { newCardLauncher.launch(NewCardActivity.getIntent(context)) }
    val onEditCardClick = { cardId: Long ->
        cardEditLauncher.launch(
            CardEditActivity.getIntent(
                context = context,
                cardId = cardId
            )
        )
    }

    PaymentsScreen(
        uiState = uiState,
        onAddCardClick = onAddCardClick,
        onEditCardClick = onEditCardClick,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@Composable
fun PaymentsScreen(
    uiState: PaymentsUiState,
    snackbarHostState: SnackbarHostState,
    onAddCardClick: () -> Unit,
    onEditCardClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is PaymentsUiState.Empty -> PaymentsEmptyScreen(
            onAddCardClick = onAddCardClick,
            snackbarHostState = snackbarHostState,
            modifier = modifier
        )

        is PaymentsUiState.One -> PaymentsOneScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onAddCardClick = onAddCardClick,
            onCardClick = onEditCardClick,
            modifier = modifier
        )

        is PaymentsUiState.Many -> PaymentsManyScreen(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onAddCardClick = onAddCardClick,
            onCardClick = onEditCardClick,
            modifier = modifier
        )
    }
}

@Composable
private fun PaymentsEmptyScreen(
    snackbarHostState: SnackbarHostState,
    onAddCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { PaymentsTopBar(isAddable = false) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                stringResource(R.string.payments_empty_headline),
                fontWeight = W700,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            PaymentCardAddition(onClick = onAddCardClick)
        }
    }
}

@Composable
private fun PaymentsOneScreen(
    uiState: PaymentsUiState.One,
    snackbarHostState: SnackbarHostState,
    onAddCardClick: () -> Unit,
    onCardClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { PaymentsTopBar(isAddable = false) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .testTag("PaymentsOneScreen"),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            PaymentCard(
                creditCard = uiState.card,
                modifier = Modifier.clickable(onClick = { onCardClick(uiState.card.id) })
            )
            Spacer(modifier = Modifier.height(32.dp))
            PaymentCardAddition(onClick = onAddCardClick, modifier = Modifier.testTag("카드 추가 버튼"))
        }
    }
}

@Composable
private fun PaymentsManyScreen(
    uiState: PaymentsUiState.Many,
    snackbarHostState: SnackbarHostState,
    onAddCardClick: () -> Unit,
    onCardClick: (id: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = { PaymentsTopBar(isAddable = true, onAddClick = onAddCardClick) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(36.dp),
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
        ) {
            items(uiState.cards, key = { it.cardNumber }) { card ->
                PaymentCard(card, modifier = Modifier.clickable(onClick = { onCardClick(card.id) }))
            }
        }
    }
}

@Preview(name = "카드가 없는 경우")
@Composable
private fun Preview1() {
    PaymentsTheme {
        PaymentsEmptyScreen(onAddCardClick = {}, snackbarHostState = SnackbarHostState())
    }
}

@Preview(name = "카드가 한 개인 경우")
@Composable
private fun Preview2() {
    PaymentsTheme {
        PaymentsOneScreen(
            uiState = PaymentsUiState.One(
                CreditCard(
                    id = -1L,
                    cardNumber = "1234567812345678",
                    expiredDate = "0101",
                    ownerName = "홍길동",
                    password = "123",
                    issuingBank = IssuingBank.HANA_CARD,
                )
            ),
            snackbarHostState = SnackbarHostState(),
            onAddCardClick = {},
            onCardClick = {}
        )
    }
}

@Preview(name = "카드가 여러 개인 경우")
@Composable
private fun Preview3() {
    PaymentsTheme {
        PaymentsManyScreen(
            uiState = PaymentsUiState.Many(
                listOf(
                    CreditCard(
                        id = -1L,
                        cardNumber = "1234567812345678",
                        expiredDate = "1231",
                        ownerName = "홍길동",
                        password = "123",
                        issuingBank = IssuingBank.KB_CARD
                    ),
                    CreditCard(
                        id = -1L,
                        cardNumber = "1234567812345648",
                        expiredDate = "1231",
                        ownerName = "홍길동",
                        password = "123",
                        issuingBank = IssuingBank.BC_CARD
                    ),
                )
            ),
            snackbarHostState = SnackbarHostState(),
            onAddCardClick = {},
            onCardClick = {}
        )
    }
}
