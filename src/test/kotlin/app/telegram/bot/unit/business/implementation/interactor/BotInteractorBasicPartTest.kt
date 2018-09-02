package app.telegram.bot.unit.business.implementation.interactor

import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.mock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class BotInteractorBasicPartTest {
    @Mock lateinit var chatManager: ChatManager
    lateinit var botInteractor: BotInteractor
    lateinit var user: ChatUser
    private var currentUser: CurrentUser = CurrentUser()

    @Before fun setUp() {
        user = ChatUser(1L, "John")
        currentUser.update(user)
        botInteractor = BotInteractorImpl(currentUser, chatManager, mock(), mock())
    }

    @Test fun sendHello_shouldCallChatManagerMethod() {
        botInteractor.sendHello()
        Mockito.verify(chatManager).sendMessage(user.chatId, "Hello, John, let's start! Use /help command for get start")
    }

    @Test fun sendHelp_shouldCallChatManagerMethod() {
        botInteractor.sendHelp()
        Mockito.verify(chatManager).sendMessage(user.chatId, """
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
        Send me ["query"] post -> Send me "Android" posts
        Send me [number] posts -> Send me 10 posts
        Send me [number] ["query"] posts -> Send me 3 "Android" posts
        """.trimIndent())
    }
}