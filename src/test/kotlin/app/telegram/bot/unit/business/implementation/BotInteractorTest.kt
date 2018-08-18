package app.telegram.bot.unit.business.implementation

import app.telegram.bot.unit.api.yahoo.WeatherApiTest
import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.business.inheritence.WeatherProvider
import app.telegram.bot.data.Weather
import app.telegram.bot.util.WeatherUtil
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class BotInteractorTest {
    @Mock lateinit var postProvider: PostProvider
    @Mock lateinit var weatherProvider: WeatherProvider
    @Mock lateinit var chatManager: ChatManager
    lateinit var botInteractor: BotInteractor
    private var chatId: Long = 299912313031498

    @Before fun before() {
        botInteractor = BotInteractorImpl(chatManager, weatherProvider, postProvider)
        chatId = Random().nextLong()
    }

    @Test fun sendHello_shouldCallChatManagerMethod() {
        botInteractor.sendHello(chatId)
        verify(chatManager).sendMessage(chatId, "Hello, let's start!")
    }


    @Test fun sendHelp_shouldCallChatManagerMethod() {
        botInteractor.sendHelp(chatId)
        verify(chatManager).sendMessage(chatId, """
        Commands:

        /start -> Send hello message
        /help -> Send this message
        /weather_current -> Send current weather in the selected region
        /weather_today -> Send today weather in the selected region
        /weather_tomorrow -> Send tomorrow weather in the selected region
        /weather_week -> Send week weather in the selected region
        /post_random -> Send random post from the top on post api site
        /posts_random -> Send random posts from the top on post api site
        /post_relevant -> Send relevant post from site api
        /posts_relevant -> Send relevant posts from site api

        Phrases:

        Send me ["query"] post -> Send me "Android" post
        Send me [number] posts -> Send me 10 posts
        Send me [number] ["query"] posts -> Send me 3 "Android" posts
        """.trimIndent())
    }

    @Test fun sendCurrentWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val currentWeather = mockWeather(Weather.Type.CURRENT)
        val mockSuccess = mockWeatherApiSuccess(currentWeather)
        `when`(weatherProvider.getCurrentWeather()).thenReturn(mockSuccess)
        botInteractor.sendCurrentWeather(chatId)
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Time: Sun, 01 Jul 2018 01:00 AM EEST
            Condition: Cloudy
            Temperature: 12 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(chatId, expect)
    }

    @Test fun sendCurrentWeatherIfError_shouldHandleError() {
        val errorMessage = "Current weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherProvider.getCurrentWeather()).thenReturn(mockError)
        botInteractor.sendCurrentWeather(chatId)
        verify(chatManager).sendMessage(chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendTodayWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val todayWeather = mockWeather(Weather.Type.TODAY)
        val mockSuccess = mockWeatherApiSuccess(todayWeather)
        `when`(weatherProvider.getTodayWeather()).thenReturn(mockSuccess)
        botInteractor.sendTodayWeather(chatId)
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(chatId, expect)
    }

    @Test fun sendTodayIfError_shouldHandleError() {
        val errorMessage = "Today weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherProvider.getTodayWeather()).thenReturn(mockError)
        botInteractor.sendTodayWeather(chatId)
        verify(chatManager).sendMessage(chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendTomorrowWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val tomorrowWeather = mockWeather(Weather.Type.TOMORROW)
        val mockSuccess = mockWeatherApiSuccess(tomorrowWeather)
        `when`(weatherProvider.getTomorrowWeather()).thenReturn(mockSuccess)
        botInteractor.sendTomorrowWeather(chatId)
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(chatId, expect)
    }

    @Test fun sendTomorrowIfError_shouldHandleError() {
        val errorMessage = "Tomorrow weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherProvider.getTomorrowWeather()).thenReturn(mockError)
        botInteractor.sendTomorrowWeather(chatId)
        verify(chatManager).sendMessage(chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendWeekWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val weekWeather = mockWeekWeather()
        val mockSuccess = mockWeekWeatherApiSuccess(weekWeather)
        `when`(weatherProvider.getWeekWeather()).thenReturn(mockSuccess)
        botInteractor.sendWeekWeather(chatId)
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
        verify(chatManager).sendMessage(chatId, expect)
    }

    @Test fun sendWeekIfError_shouldHandleError() {
        val errorMessage = "Week weather not found on yahoo api"
        val mockError = mockWeekWeatherApiError(errorMessage)
        `when`(weatherProvider.getWeekWeather()).thenReturn(mockError)
        botInteractor.sendWeekWeather(chatId)
        verify(chatManager).sendMessage(chatId, "Sorry, but occurred some error: $errorMessage")
    }

    companion object {
        fun mockWeatherApiError(textError: String) : Single<Weather> = Single.error(Throwable(textError))
        fun mockWeekWeatherApiError(textError: String) : Single<List<Weather>> = Single.error(Throwable(textError))

        fun mockWeatherApiSuccess(weather: Weather) : Single<Weather> = Single.just(weather)
        fun mockWeekWeatherApiSuccess(weatherForecast: List<Weather>) : Single<List<Weather>> = Single.just(weatherForecast)

        fun mockWeather(weatherType: Weather.Type)  : Weather {
            val jsonWrapper = WeatherApiTest.mockWeatherApiModel()
            return when(weatherType) {
                Weather.Type.CURRENT -> WeatherUtil.toCurrentWeather(jsonWrapper)
                Weather.Type.TODAY -> WeatherUtil.toTodayWeather(jsonWrapper)
                Weather.Type.TOMORROW -> WeatherUtil.toTomorrowWeather(jsonWrapper)
                else -> null!!
            }
        }
        fun mockWeekWeather() = WeatherUtil.toWeekWeather(WeatherApiTest.mockWeatherApiModel())
    }
}