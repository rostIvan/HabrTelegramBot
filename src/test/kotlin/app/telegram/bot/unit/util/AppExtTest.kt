package app.telegram.bot.unit.util

import app.telegram.bot.util.random
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.jsoup.Jsoup
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.reflect.KClass

@RunWith(JUnit4::class)
class AppExtTest {

    @Test
    fun random_inRange_shouldGenerateValidNumber1() {
        var random = (0..10).random()
        assertThat(random).isGreaterThanOrEqualTo(0).isLessThanOrEqualTo(10)

        random = (-10..10).random()
        assertThat(random).isGreaterThanOrEqualTo(-10).isLessThanOrEqualTo(10)

        random = (-2..2).random()
        assertThat(random).isGreaterThanOrEqualTo(-2).isLessThanOrEqualTo(2)

        random = (0..2).random()
        assertThat(random).isGreaterThanOrEqualTo(0).isLessThanOrEqualTo(2)

        random = (-570..0).random()
        assertThat(random).isGreaterThanOrEqualTo(-570).isLessThanOrEqualTo(0)

        random = (-570..0).random()
        assertThat(random).isGreaterThanOrEqualTo(-570).isLessThanOrEqualTo(0)

        random = (-1..100).random()
        assertThat(random).isGreaterThanOrEqualTo(-1).isLessThanOrEqualTo(100)

        random = (0..0).random()
        assertThat(random).isEqualTo(0)

        random = (0..1).random()
        assertThat(random).isGreaterThanOrEqualTo(0).isLessThanOrEqualTo(1)

        random = (-1..0).random()
        assertThat(random).isGreaterThanOrEqualTo(-1).isLessThanOrEqualTo(0)
    }

    @Test fun randomItem_inList_shouldReturnValidItem() {
        val random1 = listOf("1", "2", "3").random()
        assertThat(listOf("1", "2", "3")).contains(random1)

        val random2 = listOf(1, 2, 3, 4).random()
        assertThat(listOf(1, 2, 3, 4)).contains(random2)

        expectWithMessage(IllegalArgumentException::class, "bound must be positive") {
            listOf<Any>().random()
        }
    }

    private inline fun <T: Exception> expectWithMessage(clazz: KClass<T>, message: String = "", f: () -> Unit) {
        try {
            f.invoke()
            fail("Expect, but not called exception $clazz")
        } catch (ex: Exception) {
            assertThat(ex).isInstanceOf(clazz.java)
            if (message.isNotBlank())
                assertThat(ex.message).isEqualToIgnoringCase(message)
        }
    }
}