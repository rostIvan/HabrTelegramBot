package app.telegram.bot.unit.business.implementation

import app.telegram.bot.business.implementation.ChatManagerImpl
import app.telegram.bot.business.inheritence.ChatManager
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner.Silent::class)
class ChatManagerTest {

    @Mock lateinit var telegramBot: TelegramBot
    lateinit var chatManager: ChatManager
    var chatId: Long = 20310301241

    @Before fun before() {
        chatManager = ChatManagerImpl(telegramBot)
        chatId = Random().nextLong()
    }

    @Test fun sendMessage_shouldCalledExecutionSendingRequest() {
        chatManager.sendMessage(chatId, "Hello world!!!")
        verify(telegramBot, times(1)).execute(any(SendMessage::class.java))
        chatManager.sendMessage(chatId, "Hi!")
        verify(telegramBot, times(2)).execute(any(SendMessage::class.java))
    }

    @Test fun sendMessageWithPreview_shouldCalledExecutionSendingRequest() {
        chatManager.sendMessage(chatId, "Hello world!!!", true)
        verify(telegramBot, times(1)).execute(any(SendMessage::class.java))
        chatManager.sendMessage(chatId, "Hi!")
        verify(telegramBot, times(2)).execute(any(SendMessage::class.java))
    }
}