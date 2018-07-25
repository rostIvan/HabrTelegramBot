package app.telegram.bot.unit.business.implementation

import app.telegram.bot.business.implementation.UpdateHandlerImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class UpdateHandlerTest {
    @Mock lateinit var botInteractor: BotInteractor
    lateinit var updateHandler: UpdateHandler
    private val chatId: Long = 27455958981

    @Before fun before() {
        MockitoAnnotations.initMocks(this)
        updateHandler = UpdateHandlerImpl(botInteractor)
    }

    @Test fun onStartQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/start")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendHello(chatId)
    }

    @Test fun onCurrentWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather_current")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendCurrentWeather(chatId)
    }

    @Test fun onTodayWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather_today")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendTodayWeather(chatId)
    }

    @Test fun onTomorrowWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather_tomorrow")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendTomorrowWeather(chatId)
    }

    @Test fun onWeekWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather_week")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendWeekWeather(chatId)
    }

    private fun mockUpdate(text: String) : Update {
        val update = mock<Update>()
        val message = mock<Message>()
        `when`(message.text()).thenReturn(text)
        `when`(message.chat()).thenReturn(mock())
        `when`(message.chat().id()).thenReturn(chatId)
        `when`(update.message()).thenReturn(message)
        return update
    }

    inline fun <reified T : Any> mock() = mock(T::class.java)
}