package com.openclassrooms.realestatemanager

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun convertDollarToEuro_isCorrect() {
        val dollars = 100.0
        val expectedEuros = 81.0
        val result = Utils.convertDollarToEuro(dollars)
        val delta = 0.01

        assertEquals(expectedEuros, result, delta)
    }

    @Test
    fun convertEuroToDollar_isCorrect() {
        val euros = 100
        val expectedDollars = 123.0
        val result = Utils.convertEuroToDollar(euros)
        val delta = 0.01

        assertEquals(expectedDollars, result.toDouble(), delta)
    }

    @Test
    fun formatPrice_isCorrect() {
        assertEquals("10", Utils.formatPrice(10.0))
        assertEquals("10.5", Utils.formatPrice(10.5))
    }
}