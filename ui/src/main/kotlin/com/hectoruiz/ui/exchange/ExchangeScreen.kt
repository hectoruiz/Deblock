package com.hectoruiz.ui.exchange

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hectoruiz.domain.Constants.ANIMATION_DURATION
import com.hectoruiz.domain.Constants.MAX_DIGITS
import com.hectoruiz.domain.Constants.WALLET_BALANCE_ETHEREUM
import com.hectoruiz.domain.Currency
import com.hectoruiz.domain.ErrorState
import com.hectoruiz.ui.R
import com.hectoruiz.ui.theme.Blue
import com.hectoruiz.ui.theme.DeblockTheme
import com.hectoruiz.ui.theme.Gray
import com.hectoruiz.ui.theme.LightGray
import kotlinx.coroutines.launch

private val exchangeUiStatePreview = ExchangeUiState(
    error = ErrorState.NoError,
    loading = false,
    fee = 0.81812,
    mainCurrency = CurrencyUiModel(currency = Currency.Euro, amount = 1231.0),
)

@Preview(showBackground = true)
@Composable
fun ExchangeScreenPreview() {
    DeblockTheme {
        ExchangeScreen(
            exchangeUiState = exchangeUiStatePreview,
            onSwitchCurrency = {},
            onAllCurrencies = {},
            onSelectCurrency = {},
            onSendMoney = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(
    exchangeUiState: ExchangeUiState,
    onSwitchCurrency: (Double) -> Unit,
    onAllCurrencies: () -> Unit,
    onSelectCurrency: (Currency) -> Unit,
    onSendMoney: (CurrencyUiModel) -> Unit,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var showModal by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val rotation = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    var amount by remember {
        mutableStateOf(
            TextFieldValue(
                exchangeUiState.mainCurrency.currency.symbol + exchangeUiState.mainCurrency.amount
            )
        )
    }
    val secondaryAmount =
        if (exchangeUiState.secondaryCurrency == Currency.Ethereum) {
            amount.text.toDouble(exchangeUiState.mainCurrency.currency.symbol)
                .div(exchangeUiState.mainCurrency.currency.exchangeRate).round()
        } else {
            amount.text.toDouble(exchangeUiState.mainCurrency.currency.symbol)
                .times(exchangeUiState.secondaryCurrency.exchangeRate).round()
        }
    val isButtonEnabled =
        if (exchangeUiState.mainCurrency.currency == Currency.Ethereum) {
            val amountParsed = amount.text.toDouble(exchangeUiState.mainCurrency.currency.symbol)
            amountParsed != 0.0 && amountParsed <= WALLET_BALANCE_ETHEREUM
        } else {
            secondaryAmount != 0.0 && secondaryAmount <= WALLET_BALANCE_ETHEREUM
        }

    val realCurrency = if (exchangeUiState.mainCurrency.currency != Currency.Ethereum) {
        exchangeUiState.mainCurrency.currency
    } else {
        exchangeUiState.secondaryCurrency
    }

    LaunchedEffect(exchangeUiState.mainCurrency.currency.symbol) {
        val newCurrency =
            exchangeUiState.mainCurrency.currency.symbol + exchangeUiState.mainCurrency.amount
        amount =
            amount.copy(
                text = newCurrency,
                selection = TextRange(newCurrency.length)
            )
    }

    LaunchedEffect(exchangeUiState.error) {
        when (exchangeUiState.error) {
            is ErrorState.NetworkError -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = exchangeUiState.error.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            ErrorState.NoError -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .padding(top = 92.dp, bottom = 16.dp)
                .fillMaxSize()
        ) {
            Column {
                Text(
                    text = stringResource(id = R.string.title),
                    modifier = Modifier.padding(horizontal = 32.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(32.dp))

                if (!exchangeUiState.loading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Box {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .shadow(
                                                elevation = 4.dp,
                                                spotColor = LightGray,
                                                ambientColor = LightGray
                                            )
                                            .border(
                                                width = 2.dp,
                                                color = LightGray,
                                                shape = RoundedCornerShape(size = 8.dp)
                                            )
                                            .padding(top = 8.dp, bottom = 12.dp, end = 18.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(
                                                start = 40.dp,
                                                top = 32.dp
                                            )
                                        ) {
                                            BasicTextField(
                                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                                singleLine = true,
                                                maxLines = 1,
                                                value = amount,
                                                onValueChange = {
                                                    amount = amount.copy(
                                                        text =
                                                        if (it.text.isEmpty()) {
                                                            exchangeUiState.mainCurrency.currency.symbol
                                                        } else if (it.text != exchangeUiState.mainCurrency.currency.symbol + "." &&
                                                            it.text.count { character -> character == '.' } <= 1
                                                        ) {
                                                            it.text.take(MAX_DIGITS)
                                                        } else amount.text,
                                                        selection = if (it.text.isEmpty()) TextRange(
                                                            amount.text.length
                                                        ) else TextRange(it.text.length)
                                                    )
                                                },
                                                cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary),
                                                textStyle = MaterialTheme.typography.bodyLarge.copy(
                                                    color = MaterialTheme.colorScheme.secondary
                                                ),
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = stringResource(
                                                    id = R.string.secondary_amount,
                                                    secondaryAmount,
                                                    exchangeUiState.secondaryCurrency.name
                                                ),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Gray
                                            )
                                        }
                                        Column {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = stringResource(
                                                    id = R.string.max_amount,
                                                    WALLET_BALANCE_ETHEREUM
                                                ),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Blue
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.clickable {
                                                    onAllCurrencies()
                                                    showModal = true
                                                }) {
                                                ExchangeIcon(
                                                    currency = realCurrency,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = realCurrency.name,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Icon(
                                                    imageVector = Icons.Default.KeyboardArrowDown,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp),
                                                    tint = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                        }
                                    }
                                }
                                FilledIconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            rotation.snapTo(0f)
                                            rotation.animateTo(
                                                360f,
                                                animationSpec = tween(ANIMATION_DURATION)
                                            )
                                        }
                                        onSwitchCurrency(secondaryAmount)
                                    },
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(50.dp)
                                        .border(1.dp, LightGray, CircleShape)
                                        .clip(CircleShape)
                                        .align(Alignment.CenterStart),
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.switch_currencies),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .rotate(rotation.value),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.padding(horizontal = 32.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Info,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(
                                        id = R.string.fee_amount,
                                        exchangeUiState.fee
                                    ), style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        Button(
                            enabled = isButtonEnabled,
                            onClick = {
                                onSendMoney(
                                    CurrencyUiModel(
                                        currency = exchangeUiState.mainCurrency.currency,
                                        amount = amount.text.toDouble(exchangeUiState.mainCurrency.currency.symbol)
                                    )
                                )
                            },
                            shape = RoundedCornerShape(size = 8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                        ) {
                            Text(
                                text = stringResource(
                                    id = R.string.send,
                                    amount.text.replace(Currency.Ethereum.symbol, "")
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
            if (exchangeUiState.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    if (showModal) {
        ModalBottomSheet(
            onDismissRequest = { showModal = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            ModalExchange(
                actualCurrency = realCurrency,
                ethereumAmount = if (exchangeUiState.mainCurrency.currency == Currency.Ethereum) {
                    amount.text.toDouble(exchangeUiState.mainCurrency.currency.symbol)
                } else {
                    secondaryAmount
                },
                allCurrencies = exchangeUiState.allCurrencies,
                onSelected = onSelectCurrency
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeIconPreview() {
    DeblockTheme {
        ExchangeIcon(currency = Currency.Pound, modifier = Modifier.size(24.dp))
    }
}

@Composable
fun ExchangeIcon(currency: Currency, modifier: Modifier) {
    Icon(
        tint = Color.Unspecified,
        painter = painterResource(
            when (currency) {
                Currency.Dollar -> R.drawable.united_states
                Currency.Euro -> R.drawable.europe
                Currency.Pound -> R.drawable.united_kingdom
                else -> {
                    R.drawable.united_states
                }
            }
        ), contentDescription = null,
        modifier = modifier
    )
}

fun Double.round() = String.format(null, "%.1f", this).toDouble()

fun String.toDouble(character: String): Double {
    return if (this.substringAfter(character).isNotEmpty()) {
        try {
            this.substringAfter(character).toDouble()
        } catch (e: Exception) {
            0.0
        }
    } else {
        0.0
    }
}