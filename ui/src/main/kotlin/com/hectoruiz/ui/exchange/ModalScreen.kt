package com.hectoruiz.ui.exchange

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hectoruiz.domain.Currency
import com.hectoruiz.ui.R
import com.hectoruiz.ui.theme.DeblockTheme
import com.hectoruiz.ui.theme.Gray
import com.hectoruiz.ui.theme.LightGray

val allCurrenciesPreview = listOf(
    Currency.Dollar.apply { exchangeRate = 1669.4 },
    Currency.Pound.apply { exchangeRate = 12990.4 },
    Currency.Euro.apply { exchangeRate = 777.4 }
)

@Preview(showBackground = true)
@Composable
fun ModalExchangePreview() {
    DeblockTheme {
        ModalExchange(actualCurrency = Currency.Euro, 3.0, allCurrenciesPreview) {}
    }
}

@Composable
fun ModalExchange(
    actualCurrency: Currency,
    ethereumAmount: Double,
    allCurrencies: List<Currency>,
    onSelected: (Currency) -> Unit,
) {
    var selectedCurrency by remember { mutableStateOf(actualCurrency) }

    Column(modifier = Modifier.padding(32.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
        Text(
            text = stringResource(id = R.string.modal_title),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            ExchangeItem(
                isSelected = selectedCurrency == Currency.Euro,
                currency = allCurrencies.find { it is Currency.Euro } ?: Currency.Euro,
                descriptionName = stringResource(id = R.string.euro),
                onSelected = {
                    selectedCurrency = Currency.Euro
                    onSelected(Currency.Euro)
                },
                ethereumAmount = ethereumAmount,
            )

            ExchangeItem(
                isSelected = selectedCurrency == Currency.Dollar,
                currency = allCurrencies.find { it is Currency.Dollar } ?: Currency.Dollar,
                descriptionName = stringResource(id = R.string.dollar),
                onSelected = {
                    selectedCurrency = Currency.Dollar
                    onSelected(Currency.Dollar)
                },
                ethereumAmount = ethereumAmount
            )

            ExchangeItem(
                isSelected = selectedCurrency == Currency.Pound,
                currency = allCurrencies.find { it is Currency.Pound } ?: Currency.Pound,
                descriptionName = stringResource(id = R.string.pounds),
                onSelected = {
                    selectedCurrency = Currency.Pound
                    onSelected(Currency.Pound)
                },
                ethereumAmount = ethereumAmount
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .padding(vertical = 20.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = stringResource(id = R.string.modal_info),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun ExchangeItemPreview() {
    DeblockTheme {
        ExchangeItem(
            isSelected = false,
            currency = Currency.Pound,
            descriptionName = stringResource(id = R.string.pounds),
            ethereumAmount = 200.0,
            onSelected = {},
        )
    }
}

@Composable
fun ExchangeItem(
    isSelected: Boolean,
    currency: Currency,
    descriptionName: String,
    ethereumAmount: Double,
    onSelected: () -> Unit,
) {
    val isEnabled by remember {
        mutableStateOf(currency.exchangeRate != 0.0)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { if (isEnabled) onSelected() }
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color.Black else LightGray,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExchangeIcon(currency = currency, modifier = Modifier.size(32.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = descriptionName,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = currency.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Gray
                    )
                }
            }
        }
        Row {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = ethereumAmount.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "${
                        ethereumAmount.times(currency.exchangeRate).round()
                    } ${currency.symbol}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Gray
                )
            }
        }
    }
}
