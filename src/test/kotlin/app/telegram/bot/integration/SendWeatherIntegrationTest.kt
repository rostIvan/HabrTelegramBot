package app.telegram.bot.integration

import app.telegram.bot.Config
import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.unit.api.yahoo.WeatherApiTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(Config::class)
class SendWeatherIntegrationTest {
    @Autowired lateinit var webClient: WebTestClient
    @MockBean lateinit var weatherApi: WeatherApi
    @MockBean lateinit var chatManager: ChatManager
    val chatId: Long = 1000413156

    @Before fun before() {
        `when`(weatherApi.get(anyString())).thenReturn(WeatherApiTest.mockWeatherApiCall())
    }

    @Test fun ifUserNotCommandSend_botShouldSendNothing() {
        webhookPostReceiveMessage("1234")
        verify(chatManager, never()).sendMessage(anyLong(), anyString())
        webhookPostReceiveMessage("Common")
        verify(chatManager, never()).sendMessage(anyLong(), anyString())
        webhookPostReceiveMessage("Wrong message")
        verify(chatManager, never()).sendMessage(anyLong(), anyString())
    }


    @Test fun onStartCommand_shouldCalledSendHelloMessage() {
        webhookPostReceiveMessage("/start")
        val expect = "Hello, let's start!"
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    @Test fun onCurrentWeatherCommand_shouldSendCurrentWeather() {
        webhookPostReceiveMessage("/weather_current")
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Time: Sun, 01 Jul 2018 01:00 AM EEST
            Condition: Cloudy
            Temperature: 12 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    @Test fun onTodayWeatherCommand_shouldSendTodaytWeather() {
        webhookPostReceiveMessage("/weather_today")
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    @Test fun onTomorrowWeatherCommand_shouldSendTomorrowWeather() {
        webhookPostReceiveMessage("/weather_tomorrow")
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    @Test fun onWeekWeatherCommand_shouldSendWeekWeather() {
        webhookPostReceiveMessage("/weather_week")
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Link: https://weather.yahoo.com/country/state/city-2347539
            ===================================================
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°
            ---------------------------------------------------
            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°
            ---------------------------------------------------
            Date: 03 Jul 2018
            Day: Tue
            Condition: Partly Cloudy
            Temperature(min): 8 C°
            Temperature(max): 18 C°
            ---------------------------------------------------
            Date: 04 Jul 2018
            Day: Wed
            Condition: Partly Cloudy
            Temperature(min): 12 C°
            Temperature(max): 19 C°
            ---------------------------------------------------
            Date: 05 Jul 2018
            Day: Thu
            Condition: Partly Cloudy
            Temperature(min): 14 C°
            Temperature(max): 21 C°
            ---------------------------------------------------
            Date: 06 Jul 2018
            Day: Fri
            Condition: Rain
            Temperature(min): 15 C°
            Temperature(max): 21 C°
            ---------------------------------------------------
            Date: 07 Jul 2018
            Day: Sat
            Condition: Rain
            Temperature(min): 15 C°
            Temperature(max): 21 C°
        """.trimIndent()
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    private fun webhookPostReceiveMessage(message: String) {
        webClient.post()
                .uri("/webhook")
                .body(BodyInserters.fromObject(mockUpdateBody(message)))
                .exchange()
                .expectStatus().isOk
    }

    fun mockUpdateBody(textMessage: String) : String {
        return javaClass.getResource("/update-example.json")
                .readText()
                .replace("""text": "/start""", """text": "$textMessage""")
    }
}