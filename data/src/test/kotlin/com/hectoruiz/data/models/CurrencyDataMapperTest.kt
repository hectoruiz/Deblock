package com.hectoruiz.data.models

import com.hectoruiz.domain.Currency
import org.junit.Assert.assertEquals
import org.junit.Test

class CurrencyDataMapperTest {

    @Test
    fun `null currency api model to currency model`() {
        val currencyResponseApiModel: CurrencyResponseApiModel? = null
        val currencyModel = currencyResponseApiModel.toModel()

        assertEquals(Currency.Dollar, currencyModel.currency)
        assertEquals(0.0, currencyModel.currency.exchangeRate, 0.0)
    }

    @Test
    fun `currency api model null to fee model`() {
        val currencyResponseApiModel = CurrencyResponseApiModel(null)
        val currencyModel = currencyResponseApiModel.toModel()

        assertEquals(Currency.Dollar, currencyModel.currency)
        assertEquals(0.0, currencyModel.currency.exchangeRate, 0.0)
    }

    @Test
    fun `currency euro api model to fee model`() {
        val currencyResponseApiModel =
            CurrencyResponseApiModel(ethereum = mapOf(Pair("eur", 555.0)))
        val currencyModel = currencyResponseApiModel.toModel()

        assertEquals(Currency.Euro, currencyModel.currency)
        assertEquals(555.0, currencyModel.currency.exchangeRate, 0.0)
    }

    @Test
    fun `currency dollar api model to fee model`() {
        val currencyResponseApiModel =
            CurrencyResponseApiModel(ethereum = mapOf(Pair("usd", 555.0)))
        val currencyModel = currencyResponseApiModel.toModel()

        assertEquals(Currency.Dollar, currencyModel.currency)
        assertEquals(555.0, currencyModel.currency.exchangeRate, 0.0)
    }

    @Test
    fun `currency pound api model to fee model`() {
        val currencyResponseApiModel =
            CurrencyResponseApiModel(ethereum = mapOf(Pair("gbp", 555.0)))
        val currencyModel = currencyResponseApiModel.toModel()

        assertEquals(Currency.Pound, currencyModel.currency)
        assertEquals(555.0, currencyModel.currency.exchangeRate, 0.0)
    }

    @Test
    fun `null fee response api model to fee model`() {
        val feeResponseApiModel: FeeResponseApiModel? = null
        val feeModel = feeResponseApiModel.toModel()

        assertEquals(0.0, feeModel.gasPrice, 0.0)
    }

    @Test
    fun `currency fee response api model null to currency model`() {
        val feeResponseApiModel = FeeResponseApiModel(null)
        val feeModel = feeResponseApiModel.toModel()

        assertEquals(0.0, feeModel.gasPrice, 0.0)
    }

    @Test
    fun `currency fee response api model to currency model`() {
        val feeApiModel = FeeApiModel(gasPrice = 33.0)
        val feeResponseApiModel = FeeResponseApiModel(feeApiModel)
        val feeModel = feeResponseApiModel.toModel()

        val calcFee = 21000 * 33.0 / 100000000
        assertEquals(calcFee, feeModel.gasPrice, 0.0)
    }
}
