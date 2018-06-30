package app.telegram.bot

import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.data.Weather
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(Config::class)
class WeatherApiTest {
    @Autowired lateinit var weatherApi: WeatherApi

    @Test fun getAPI() {
        val single = weatherApi.get()
        val channel = single.blockingGet().query.results.channel
        val condition = channel.item.condition
        val temperature = condition.temperature
        val status = condition.status

        println(temperature)
        println(status)

        val weather = Weather(
                "${channel.location.city}, ${channel.location.region.trim()}",
                channel.link.replaceBeforeLast("*", "").replace("*", ""),
                condition.date,
                condition.status,
                temperature,
                temperature)
        println(weather)

        assertThat(temperature).isGreaterThan(-100)
        assertThat(status).isNotNull()
        assertThat(status).doesNotContainOnlyWhitespaces()
    }
}