package app.telegram.bot

import app.telegram.bot.business.implementation.UpdateHandlerImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
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

    @Before fun before() {
        MockitoAnnotations.initMocks(this)
        updateHandler = UpdateHandlerImpl(botInteractor)
    }

    @Test fun onTodayWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather/today")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendTodayWeather()
    }

    @Test fun onTomorrowWeatherQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather/tomorrow")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendTomorrowWeather()
    }

    @Test fun onQueryWeekWeather_shouldCalledCorrectMethod() {
        val update = mockUpdate("/weather/week")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendWeekWeather()
    }

    private fun mockUpdate(text: String) : Update {
        val update = mock(Update::class.java)
        val message = mock(Message::class.java)
        `when`(message.text()).thenReturn(text)
        `when`(update.message()).thenReturn(message)
        return update
    }

    @After fun after() {  }
}