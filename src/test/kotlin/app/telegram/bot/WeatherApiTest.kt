package app.telegram.bot

import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.api.yahoo.WeatherApi.Companion.defaultLocation
import app.telegram.bot.api.yahoo.WeatherJsonWrapper
import com.google.gson.Gson
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import java.io.File

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Config::class)
class WeatherApiTest {
    @Autowired lateinit var weatherApi: WeatherApi

    @Test fun getAPI_shouldProvideNotNullChannel() {
        val request = weatherApi.get(defaultLocation)
        val jsonWrapper = request.blockingGet()
        assertThat(jsonWrapper.query).isNotNull
        assertThat(jsonWrapper.query.results).isNotNull
        assertThat(jsonWrapper.query.results.channel).isNotNull
    }

    @Test fun getChanel_shouldReturnObjectWithNotNullProperties() {
        val request = weatherApi.get(defaultLocation)
        val jsonWrapper = request.blockingGet()
        val channel = jsonWrapper.query.results.channel
        assertThat(channel.location).isNotNull
        assertThat(channel.location.country).isNotBlank()
        assertThat(channel.location.city).isNotBlank()
        assertThat(channel.location.region).isNotBlank()
        assertThat(channel.link).isNotBlank()
        assertThat(channel.item).isNotNull
    }

    @Test fun getCurrentWeatherCondition_shouldReturnNotEmptyValues() {
        val request = weatherApi.get(defaultLocation)
        val jsonWrapper = request.blockingGet()
        val currentCondition = jsonWrapper.query.results.channel.item.currentCondition
        assertThat(currentCondition.date).isNotBlank()
        assertThat(currentCondition.status).isNotBlank()
        assertThat(currentCondition.temperature).isLessThan(100).isGreaterThan(-100)
    }

    @Test fun getForecast_shouldReturnNotEmptyValidValues() {
        val request = weatherApi.get(defaultLocation)
        val jsonWrapper = request.blockingGet()
        val forecast = jsonWrapper.query.results.channel.item.forecast
        assertThat(forecast).isNotEmpty
        assertThat(forecast.size).isGreaterThanOrEqualTo(7)
        for (f in forecast) {
            assertThat(f.date).isNotBlank()
            assertThat(f.status).isNotBlank()
            assertThat(f.temperatureMin).isGreaterThan(-100).isLessThan(100)
            assertThat(f.temperatureMax).isGreaterThan(-100).isLessThan(100)
        }
    }

    @Test fun getApi_shouldCorrectParseJsonToDTO() {
        val call = mockWeatherApiCall()
        val jsonWrapper = call.blockingGet()
        val channel = jsonWrapper.query.results.channel
        assertThat(channel.link).isEqualTo("https://weather.yahoo.com/country/state/city-2347539/")

        assertThat(channel.location.country).isEqualTo("Ukraine")
        assertThat(channel.location.region).isEqualTo("UA")
        assertThat(channel.location.city).isEqualTo("Ivano-Frankivsk Oblast")

        val currentCondition = channel.item.currentCondition
        assertThat(currentCondition.date).isEqualTo("Sun, 01 Jul 2018 01:00 AM EEST")
        assertThat(currentCondition.status).isEqualTo("Cloudy")
        assertThat(currentCondition.temperature).isEqualTo(53)

        val forecast = channel.item.forecast
        assertThat(forecast).isNotNull.isNotEmpty.hasSize(10)

        val forecastFirst = forecast.first()
        val forecastLast = forecast.last()
        assertThat(forecastFirst.date).isEqualTo("01 Jul 2018")
        assertThat(forecastFirst.day).isEqualTo("Sun")
        assertThat(forecastFirst.temperatureMin).isEqualTo(49)
        assertThat(forecastFirst.temperatureMax).isEqualTo(63)
        assertThat(forecastFirst.status).isEqualTo("Mostly Cloudy")

        assertThat(forecastLast.date).isEqualTo("10 Jul 2018")
        assertThat(forecastLast.day).isEqualTo("Tue")
        assertThat(forecastLast.temperatureMin).isEqualTo(56)
        assertThat(forecastLast.temperatureMax).isEqualTo(70)
        assertThat(forecastLast.status).isEqualTo("Partly Cloudy")
    }

    companion object {
        fun mockWeatherApiCall() : Single<WeatherJsonWrapper> {
            val json = mockJsonResponse()
            val jsonWrapper = Gson().fromJson(json, WeatherJsonWrapper::class.java)
            return Single.just(jsonWrapper)
        }

        fun mockJsonResponse() = File(WeatherProviderTest::class.java.getResource("/static/weather-example.json").path)
                .inputStream()
                .readBytes()
                .toString(Charsets.UTF_8)
    }
}