package app.telegram.bot.unit.data.service

import app.telegram.bot.data.api.yahoo.WeatherApi
import app.telegram.bot.data.api.yahoo.WeatherApi.Companion.defaultLocation
import app.telegram.bot.data.service.weather.WeatherServiceImpl
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.unit.data.api.yahoo.WeatherApiTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class WeatherServiceTest {
    @Mock lateinit var weatherApi: WeatherApi
    lateinit var weatherService: WeatherService

    @Before fun before() {
        MockitoAnnotations.initMocks(this)
        `when`(weatherApi.get(defaultLocation)).thenReturn(WeatherApiTest.mockWeatherApiCall())
        weatherService = WeatherServiceImpl(weatherApi)
    }

    @Test fun getCurrentWeather_shouldReturnValidSingle() {
        val currentWeather = weatherService.getCurrentWeather()
        assertThat(currentWeather).isNotNull
        currentWeather.blockingGet().let { current ->
            assertThat(current.location).isEqualTo("Ivano-Frankivsk Oblast, Ukraine(UA)")
            assertThat(current.link).isEqualTo("https://weather.yahoo.com/country/state/city-2347539")
            assertThat(current.date).isEqualTo("Sun, 01 Jul 2018 01:00 AM EEST")
            assertThat(current.day).isBlank()
            assertThat(current.condition).isEqualTo("Cloudy")
            assertThat(current.temperatureMin).isEqualTo(current.temperatureMax).isEqualTo(12)
        }
    }

    @Test fun getTodayWeather_shouldReturnValidSingle() {
        val todayWeather = weatherService.getTodayWeather()
        assertThat(todayWeather).isNotNull
        todayWeather.blockingGet().let { today ->
            assertThat(today.location).isEqualTo("Ivano-Frankivsk Oblast, Ukraine(UA)")
            assertThat(today.link).isEqualTo("https://weather.yahoo.com/country/state/city-2347539")
            assertThat(today.date).isEqualTo("01 Jul 2018")
            assertThat(today.day).isEqualTo("Sun")
            assertThat(today.condition).isEqualTo("Mostly Cloudy")
            assertThat(today.temperatureMin).isEqualTo(9)
            assertThat(today.temperatureMax).isEqualTo(17)
        }
    }

    @Test fun getTomorrowWeather_shouldReturnValidSingle() {
        val tomorrowWeather = weatherService.getTomorrowWeather()
        assertThat(tomorrowWeather).isNotNull
        tomorrowWeather.blockingGet().let { tomorrow ->
            assertThat(tomorrow.location).isEqualTo("Ivano-Frankivsk Oblast, Ukraine(UA)")
            assertThat(tomorrow.link).isEqualTo("https://weather.yahoo.com/country/state/city-2347539")
            assertThat(tomorrow.date).isEqualTo("02 Jul 2018")
            assertThat(tomorrow.day).isEqualTo("Mon")
            assertThat(tomorrow.condition).isEqualTo("Mostly Cloudy")
            assertThat(tomorrow.temperatureMin).isEqualTo(9)
            assertThat(tomorrow.temperatureMax).isEqualTo(16)
        }
    }

    @Test fun getWeekWeather_shouldReturnValidSingle() {
        val weekWeather = weatherService.getWeekWeather()
        assertThat(weekWeather).isNotNull
        weekWeather.blockingGet().let { week ->
            assertThat(week).isNotNull.isNotEmpty.hasSize(7)
            val today = weatherService.getTodayWeather().blockingGet()
            val tomorrow = weatherService.getTomorrowWeather().blockingGet()
            assertThat(week[0]).isNotNull.isEqualTo(today)
            assertThat(week[1]).isNotNull.isEqualTo(tomorrow)
        }
    }
}