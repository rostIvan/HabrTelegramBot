package app.telegram.bot.unit.business.implementation.interactor

import app.telegram.bot.unit.data.api.yahoo.WeatherApiTest
import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.data.model.Weather
import app.telegram.bot.mock
import app.telegram.bot.util.WeatherUtil
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class BotInteractorWeatherPartTest {
    @Mock lateinit var weatherService: WeatherService
    @Mock lateinit var chatManager: ChatManager
    lateinit var botInteractor: BotInteractor
    lateinit var user: ChatUser
    private val currentUser = CurrentUser()

    @Before fun before() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        user = ChatUser(2301959195L, "Nick")
        currentUser.update(user)
        botInteractor = BotInteractorImpl(currentUser, chatManager, weatherService, mock())
    }

    @Test fun sendCurrentWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val currentWeather = mockWeather(Weather.Type.CURRENT)
        val mockSuccess = mockWeatherApiSuccess(currentWeather)
        `when`(weatherService.getCurrentWeather()).thenReturn(mockSuccess)
        botInteractor.sendCurrentWeather()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Time: Sun, 01 Jul 2018 01:00 AM EEST
            Condition: Cloudy
            Temperature: 12 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(user.chatId, expect)
    }

    @Test fun sendCurrentWeatherIfError_shouldHandleError() {
        val errorMessage = "Current weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherService.getCurrentWeather()).thenReturn(mockError)
        botInteractor.sendCurrentWeather()
        verify(chatManager).sendMessage(user.chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendTodayWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val todayWeather = mockWeather(Weather.Type.TODAY)
        val mockSuccess = mockWeatherApiSuccess(todayWeather)
        `when`(weatherService.getTodayWeather()).thenReturn(mockSuccess)
        botInteractor.sendTodayWeather()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 01 Jul 2018
            Day: Sun
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 17 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(user.chatId, expect)
    }

    @Test fun sendTodayIfError_shouldHandleError() {
        val errorMessage = "Today weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherService.getTodayWeather()).thenReturn(mockError)
        botInteractor.sendTodayWeather()
        verify(chatManager).sendMessage(user.chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendTomorrowWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val tomorrowWeather = mockWeather(Weather.Type.TOMORROW)
        val mockSuccess = mockWeatherApiSuccess(tomorrowWeather)
        `when`(weatherService.getTomorrowWeather()).thenReturn(mockSuccess)
        botInteractor.sendTomorrowWeather()
        val expect = """
            Location: Ivano-Frankivsk Oblast, Ukraine(UA)
            Date: 02 Jul 2018
            Day: Mon
            Condition: Mostly Cloudy
            Temperature(min): 9 C°
            Temperature(max): 16 C°
            Link: https://weather.yahoo.com/country/state/city-2347539
        """.trimIndent()
        verify(chatManager).sendMessage(user.chatId, expect)
    }

    @Test fun sendTomorrowIfError_shouldHandleError() {
        val errorMessage = "Tomorrow weather not found on yahoo api"
        val mockError = mockWeatherApiError(errorMessage)
        `when`(weatherService.getTomorrowWeather()).thenReturn(mockError)
        botInteractor.sendTomorrowWeather()
        verify(chatManager).sendMessage(user.chatId, "Sorry, but occurred some error: $errorMessage")
    }

    @Test fun sendWeekWeatherIfSuccess_shouldCallSendWeatherMethod() {
        val weekWeather = mockWeekWeather()
        val mockSuccess = mockWeekWeatherApiSuccess(weekWeather)
        `when`(weatherService.getWeekWeather()).thenReturn(mockSuccess)
        botInteractor.sendWeekWeather()
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
        verify(chatManager).sendMessage(user.chatId, expect)
    }

    @Test fun sendWeekIfError_shouldHandleError() {
        val errorMessage = "Week weather not found on yahoo api"
        val mockError = mockWeekWeatherApiError(errorMessage)
        `when`(weatherService.getWeekWeather()).thenReturn(mockError)
        botInteractor.sendWeekWeather()
        verify(chatManager).sendMessage(user.chatId, "Sorry, but occurred some error: $errorMessage")
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