package app.telegram.bot.unit.util

import app.telegram.bot.util.random
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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

}