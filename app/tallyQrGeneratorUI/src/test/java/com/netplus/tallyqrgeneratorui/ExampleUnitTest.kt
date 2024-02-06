package com.netplus.tallyqrgeneratorui

import com.netplus.coremechanism.utils.getCardScheme
import com.netplus.coremechanism.utils.getCardType
import com.netplus.coremechanism.utils.isValidCardNumber
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun validate_card_info() {
        val cardNumber = "5078729048080840".replace("-", "")

        val cardType = getCardType(cardNumber)
        if (isValidCardNumber(cardNumber, cardType)) {
            val cardScheme = getCardScheme(cardNumber)
            println("Card Type: $cardType\nCard Scheme: $cardScheme")
        } else {
            println("Invalid card")
        }
    }
}