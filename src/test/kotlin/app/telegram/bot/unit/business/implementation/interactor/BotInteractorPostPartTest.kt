package app.telegram.bot.unit.business.implementation.interactor

import app.telegram.bot.business.error.NoPostsFound
import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.model.Post
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.mock
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
class BotInteractorPostPartTest {
    @Mock lateinit var postService: PostService
    @Mock lateinit var chatManager: ChatManager
    lateinit var botInteractor: BotInteractor
    lateinit var user: ChatUser
    private val currentUser = CurrentUser()

    @Before fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        user = ChatUser(10059130L, "John")
        currentUser.update(user)
        botInteractor = BotInteractorImpl(currentUser, chatManager, mock(), postService)
    }

    @Test fun getRandomPost_ifSuccess_shouldSendCorrectMessage() {
        val expectedMessage = "> https://habr.com/post/311412/"
        verifySendMessage(postService.getRandomPost(), mockRandomPost(), expectedMessage, preview = true) {
            botInteractor.sendRandomPost()
        }
    }

    @Test fun getRandomPost_ifNotSuccess_shouldSendCorrectMessage() {
        verifyErrorMessage(postService.getRandomPost()) {
            botInteractor.sendRandomPost()
        }
    }

    @Test fun getPostByQuery_ifSuccess_shouldSendCorrectMessage() {
        val query = "Map"
        val expectedMessage = "> https://habr.com/post/131249/"
        verifySendMessage(postService.getPostByQuery(query), mockPostByQuery(), expectedMessage, preview = true) {
            botInteractor.sendPostByQuery(query)
        }
    }

    @Test fun getPostByQuery_ifNotSuccess_shouldSendCorrectMessage() {
        val query = "Weather API"
        verifyErrorMessage(postService.getPostByQuery(query)) {
            botInteractor.sendPostByQuery(query)
        }
    }

    @Test fun getRandomPosts_ifSuccess_shouldSendCorrectMessage() {
        val count = 2
        val expectedMessage = """
            Flutter I — Введение и установка
            > https://habr.com/post/311412/

            Примеры работы с разными map API
            > https://habr.com/post/131249/
        """.trimIndent()
        verifySendMessage(postService.getRandomPosts(count), mockRandomPosts(), expectedMessage) {
            botInteractor.sendRandomPosts(count)
        }
    }

    @Test fun getRandomPosts_ifNotSuccess_shouldSendCorrectMessage() {
        val count = 5
        verifyErrorMessage(postService.getRandomPosts(count)) {
            botInteractor.sendRandomPosts(count)
        }
    }

    @Test fun getPostsByQuery_ifSuccess_shouldSendCorrectMessage() {
        val query = "GoLang"
        val count = 2
        val expectedMessage = """
            Ускоряем приложение Android с помощью Golang
            > https://habr.com/post/260609/

            Грузите апельсины бочках. Релизы в Golang проектах
            > https://habr.com/post/347094/
        """.trimIndent()
        verifySendMessage(postService.getPostsByQuery(query, count), mockPostsByQuery(), expectedMessage) {
            botInteractor.sendPostsByQuery(query, count)
        }
    }

    @Test fun getPostsByQuery_ifNotSuccess_shouldSendCorrectMessage() {
        val query = "ADD__"
        val count = 1000
        verifyErrorMessage(postService.getPostsByQuery(query, count)) {
            botInteractor.sendPostsByQuery(query, count)
        }
    }

    private fun <T> verifySendMessage(request: Single<T>, mock: Single<T>, message: String, preview: Boolean = false, f: () -> Unit) {
        `when`(request).thenReturn(mock)
        f.invoke()
        verify(chatManager, times(1)).sendMessage(user.chatId, text = message, preview = preview)
    }

    private fun <T> verifyErrorMessage(postSingle: Single<T>, f: () -> Unit) {
        `when`(postSingle).thenReturn(mockNoPostFoundError())
        f.invoke()
        val expectedMessage = "Sorry, but occurred some error: Can't find posts"
        verify(chatManager, times(1)).sendMessage(user.chatId, text = expectedMessage, preview = false)
    }

    private fun <T> mockNoPostFoundError(): Single<T> = Single.error(NoPostsFound())
    private fun mockRandomPost(): Single<Post> = Single.just(Post("Flutter I — Введение и установка", "https://habr.com/post/311412/"))
    private fun mockPostByQuery(): Single<Post> = Single.just(Post("Примеры работы с разными map API", "https://habr.com/post/131249/"))
    private fun mockRandomPosts(): Single<List<Post>> = Single.just(listOf(
            Post("Flutter I — Введение и установка", "https://habr.com/post/311412/"),
            Post("Примеры работы с разными map API", "https://habr.com/post/131249/")
    ))
    private fun mockPostsByQuery(): Single<List<Post>> = Single.just(listOf(
            Post("Ускоряем приложение Android с помощью Golang", "https://habr.com/post/260609/"),
            Post("Грузите апельсины бочках. Релизы в Golang проектах", "https://habr.com/post/347094/")
    ))
}