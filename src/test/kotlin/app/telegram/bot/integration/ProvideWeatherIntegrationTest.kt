package app.telegram.bot.integration

import app.telegram.bot.unit.data.api.yahoo.WeatherApiTest
import app.telegram.bot.data.api.yahoo.WeatherApi
import app.telegram.bot.data.service.weather.WeatherServiceImpl
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.util.toMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class ProvideWeatherIntegrationTest {
    @Mock lateinit var weatherApi: WeatherApi
    lateinit var weatherService: WeatherService

    @Before fun before() {
        MockitoAnnotations.initMocks(this)
        mockWeatherApi()
        weatherService = WeatherServiceImpl(weatherApi)
    }

    @Test fun toMessage_shouldReturnFormattedCurrentWeatherMessage() {
        val weather = weatherService.getCurrentWeather().blockingGet()
        val message = weather.toMessage()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Time: Sun, 01 Jul 2018 01:00 AM EEST
            Condition: Cloudy
            Temperature: 12 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        assertThat(message).isEqualTo(expect)
    }

    @Test fun toMessage_shouldReturnFormattedTodayWeatherMessage() {
        val weather = weatherService.getTodayWeather().blockingGet()
        val message = weather.toMessage()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        assertThat(message).isEqualTo(expect)
    }

    @Test fun toMessage_shouldReturnFormattedTomorrowWeatherMessage() {
        val weather = weatherService.getTomorrowWeather().blockingGet()
        val message = weather.toMessage()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        assertThat(message).isEqualTo(expect)
    }

    @Test fun toMessage_shouldReturnFormattedWeekWeatherForecastMessage() {
        val weather = weatherService.getWeekWeather().blockingGet()
        val message = weather.toMessage()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Link: https://weather.yahoo.com/country/state/city-2347539

            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°

            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°

            Date: 03 Jul 2018
            Day: Tue
            Condition: Partly Cloudy
            Temperature(min): 8 C°
            Temperature(max): 18 C°

            Date: 04 Jul 2018
            Day: Wed
            Condition: Partly Cloudy
            Temperature(min): 12 C°
            Temperature(max): 19 C°

            Date: 05 Jul 2018
            Day: Thu
            Condition: Partly Cloudy
            Temperature(min): 14 C°
            Temperature(max): 21 C°

            Date: 06 Jul 2018
            Day: Fri
            Condition: Rain
            Temperature(min): 15 C°
            Temperature(max): 21 C°

            Date: 07 Jul 2018
            Day: Sat
            Condition: Rain
            Temperature(min): 15 C°
            Temperature(max): 21 C°
        """.trimIndent()
        assertThat(message).isEqualTo(expect)
    }

    private fun mockWeatherApi() {
        val weatherApiCall = WeatherApiTest.mockWeatherApiCall()
        `when`(weatherApi.get(anyString())).thenReturn(weatherApiCall)
    }
}