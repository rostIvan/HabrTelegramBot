package app.telegram.bot.unit.util

import app.telegram.bot.data.Weather
import app.telegram.bot.util.toMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MessageExtTest {

    @Rule @JvmField val expectedExceptionRule = ExpectedException.none()!!

    @Test fun toMessage_shouldReturnPreparedMessageWithCurrentWeather() {
        val weather = getCurrentWeatherModel()
        val message = weather.toMessage()
        checkOnNullAndBlank(message)
        val except = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Time: Sun, 01 Jul 2018 01:00 AM EEST
            Condition: Cloudy
            Temperature: 11 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        assertThat(message).isEqualTo(except)
    }

    @Test fun toMessage_shouldReturnPreparedMessageWithForecastWeather() {
        val weather = getDayForecastWeatherModel()
        val message = weather.toMessage()
        checkOnNullAndBlank(message)
        val except = """
            Location: Tokyo, Japan(Tokyo Prefecture)
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 13 C°
            Temperature(max): 27 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        assertThat(message).isEqualTo(except)
    }

    @Test fun toMessageIfIncorrectArgument_shouldThrowException() {
        val list = listOf("It's", "array", "of", "string")
        expectedExceptionRule.expect(IllegalArgumentException::class.java)
        expectedExceptionRule.expectMessage("It isn't valid objects for transform to message (String)")
        list.toMessage()
    }

    @Test fun toMessage_shouldReturnPreparedMessageWeekForecast() {
        val weather = getWeekForecastModel()
        val message = weather.toMessage()
        checkOnNullAndBlank(message)
        val except = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Link: https://weather.yahoo.com/country/state/city-2347539
            ===================================================
            Date: 01 Jul 2018
            Day: Sun
            Condition: Cloudy
            Temperature(min): 13 C°
            Temperature(max): 27 C°
            ---------------------------------------------------
            Date: 02 Jul 2018
            Day: Mon
            Condition: Partly Cloudy
            Temperature(min): 11 C°
            Temperature(max): 17 C°
            ---------------------------------------------------
            Date: 03 Jul 2018
            Day: Tue
            Condition: Rain
            Temperature(min): 5 C°
            Temperature(max): 10 C°
            ---------------------------------------------------
            Date: 04 Jul 2018
            Day: Wed
            Condition: Rain
            Temperature(min): 24 C°
            Temperature(max): 30 C°
            ---------------------------------------------------
            Date: 05 Jul 2018
            Day: Thu
            Condition: Mostly Cloudy
            Temperature(min): 4 C°
            Temperature(max): 8 C°
            ---------------------------------------------------
            Date: 06 Jul 2018
            Day: Fri
            Condition: Snow
            Temperature(min): -12 C°
            Temperature(max): -2 C°
            ---------------------------------------------------
            Date: 07 Jul 2018
            Day: Sat
            Condition: Thunderstorm
            Temperature(min): 11 C°
            Temperature(max): 17 C°
        """.trimIndent()
        assertThat(message).isEqualTo(except)
    }

    private fun getCurrentWeatherModel() = Weather(
            location = "Ivano-Frankivsk Oblast, Ukraine(UA)",
            date = "Sun, 01 Jul 2018 01:00 AM EEST",
            condition = "Cloudy",
            temperatureMin = 11,
            temperatureMax = 11,
            link = "https://weather.yahoo.com/country/state/city-2347539")

    private fun getDayForecastWeatherModel() = Weather(
            location = "Tokyo, Japan(Tokyo Prefecture)",
            date = "01 Jul 2018",
            day = "Sun",
            condition = "Mostly Cloudy",
            temperatureMin = 13,
            temperatureMax = 27,
            link = "https://weather.yahoo.com/country/state/city-2347539")

    private fun getWeekForecastModel(): List<Weather> {
        val location = "Ivano-Frankivsk Oblast, Ukraine(UA)"
        val link = "https://weather.yahoo.com/country/state/city-2347539"
        return listOf(
                Weather(location, link, "01 Jul 2018", "Sun", "Cloudy", 13, 27),
                Weather(location, link, "02 Jul 2018", "Mon", "Partly Cloudy", 11, 17),
                Weather(location, link, "03 Jul 2018", "Tue", "Rain", 5, 10),
                Weather(location, link, "04 Jul 2018", "Wed", "Rain", 24, 30),
                Weather(location, link, "05 Jul 2018", "Thu", "Mostly Cloudy", 4, 8),
                Weather(location, link, "06 Jul 2018", "Fri", "Snow", -12, -2),
                Weather(location, link, "07 Jul 2018", "Sat", "Thunderstorm", 11, 17)
        )
    }

    private fun checkOnNullAndBlank(message: String?) {
        assertThat(message).isNotNull()
        assertThat(message).isNotBlank()
    }
}