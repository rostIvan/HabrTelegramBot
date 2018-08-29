package app.telegram.bot.module

import app.telegram.bot.Config
import app.telegram.bot.data.api.yahoo.WeatherApi
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.unit.data.api.yahoo.WeatherApiTest
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(Config::class)
class SendWeatherModuleTest {
    @Autowired lateinit var webClient: WebTestClient
    @MockBean lateinit var weatherApi: WeatherApi
    @MockBean lateinit var chatManager: ChatManager
    lateinit var mockWebClient: MockWebClient

    val chatId: Long = 1000413156

    @Before fun before() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        `when`(weatherApi.get(anyString())).thenReturn(WeatherApiTest.mockWeatherApiCall())
        mockWebClient = MockWebClient(webClient)
    }

    @Test fun ifUserNotCommandSend_botShouldSendNothing() {
        mockWebClient.webhookPostReceiveMessage("1234")
        verify(chatManager, never()).sendMessage(anyLong(), anyString(), anyBoolean())
        mockWebClient.webhookPostReceiveMessage("Common")
        verify(chatManager, never()).sendMessage(anyLong(), anyString(), anyBoolean())
        mockWebClient.webhookPostReceiveMessage("Wrong message")
        verify(chatManager, never()).sendMessage(anyLong(), anyString(), anyBoolean())
    }


    @Test fun onStartCommand_shouldCalledSendHelloMessage() {
        mockWebClient.webhookPostReceiveMessage("/start")
        val expect = "Hello, jiayu, let's start! Use /help command for get start"
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }

    @Test fun onCurrentWeatherCommand_shouldSendCurrentWeather() {
        mockWebClient.webhookPostReceiveMessage("/weather_current")
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
        mockWebClient.webhookPostReceiveMessage("/weather_today")
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
        mockWebClient.webhookPostReceiveMessage("/weather_tomorrow")
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
        mockWebClient.webhookPostReceiveMessage("/weather_week")
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
        verify(chatManager, times(1)).sendMessage(chatId, expect)
    }
}