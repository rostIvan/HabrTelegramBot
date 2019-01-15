package app.telegram.bot.unit.business.implementation

import app.telegram.bot.business.implementation.UpdateHandlerImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.mock
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
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
    private val currentUser = CurrentUser()

    @Before fun before() {
        MockitoAnnotations.initMocks(this)
        currentUser.update(ChatUser(chatId, "Alex"))
        updateHandler = UpdateHandlerImpl(currentUser, botInteractor)
    }

    @Test fun onStartQuery_shouldCalledCorrectMethod() {
        val update = mockUpdate("/start")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendHello()
    }

    @Test fun onHelpCommand_shouldCalledCorrectMethod() {
        var update = mockUpdate("/help")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendHelp()

        update = mockUpdate("-h")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(2)).sendHelp()

        update = mockUpdate("--help")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(3)).sendHelp()
    }

    @Test fun onStartCommand_shouldCalledCorrectMethod() {
        val update = mockUpdate("/start")
        updateHandler.onUpdateReceive(update)
        verify(botInteractor, times(1)).sendHello()
    }

    private fun mockUpdate(text: String) : Update {
        val update = mock<Update>()
        val message = mock<Message>()
        `when`(message.text()).thenReturn(text)
        `when`(message.chat()).thenReturn(mock())
        `when`(message.chat().id()).thenReturn(chatId)
        `when`(message.chat().username()).thenReturn("Alex")
        `when`(update.message()).thenReturn(message)
        return update
    }
}