package app.telegram.bot.module

import app.telegram.bot.Config
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.Post
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.data.storage.dao.DaoStorage
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
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
import kotlin.concurrent.timer

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(Config::class)
class SendPostModuleTest {
    @Autowired lateinit var webClient: WebTestClient
    @MockBean lateinit var postService: PostService
    @MockBean lateinit var chatManager: ChatManager
    lateinit var mockWebClient: MockWebClient

    @Before fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        mockWebClient = MockWebClient(webClient)
    }

    @Test fun onRandomPost_shouldSendValidMessage() {
        `when`(postService.getRandomPost()).thenReturn(
                Single.just(Post("Rust. Best practices", "http://habr.com/post20035")),
                Single.just(Post("Python for beginners", "http://habr.com/post12005"))
        )

        mockWebClient.webhookPostReceiveMessage("/post_random", "John", 1000L)
        verify(chatManager, times(1)).sendMessage(1000L, "> http://habr.com/post20035", preview = true)

        mockWebClient.webhookPostReceiveMessage("/post_random", "Alex", 2000L)
        verify(chatManager, times(1)).sendMessage(2000L, "> http://habr.com/post12005", preview = true)
    }

    @Test fun onRandomPosts_shouldSendValidMessage() {
        `when`(postService.getRandomPosts()).thenReturn(
                Single.just(listOf(
                        Post("Rust. Best practices", "http://habr.com/post1234"),
                        Post("JVM lang", "http://habr.com/post20035"),
                        Post("Go and performance", "http://habr.com/post5678"))
                ),
                Single.just(listOf(
                        Post("R for data science", "http://habr.com/post20035"),
                        Post("Java annotation", "http://habr.com/post91005"),
                        Post("Kotlin inline function", "http://habr.com/post12356"))
                )
        )
        `when`(postService.getRandomPosts(5)).thenReturn(
                Single.just(listOf(
                        Post("Rust. Best practices", "http://habr.com/post20035"),
                        Post("JVM lang", "http://habr.com/post91005"),
                        Post("Go and performance", "http://habr.com/post13455"),
                        Post("Java annotation", "http://habr.com/post11356"),
                        Post("Kotlin inline function", "http://habr.com/post31456"))
                )
        )
        val expected1 = """
            Rust. Best practices
            > http://habr.com/post1234

            JVM lang
            > http://habr.com/post20035

            Go and performance
            > http://habr.com/post5678
        """.trimIndent()
        val expected2 = """
            R for data science
            > http://habr.com/post20035

            Java annotation
            > http://habr.com/post91005

            Kotlin inline function
            > http://habr.com/post12356
        """.trimIndent()

        val expected3 = """
            Rust. Best practices
            > http://habr.com/post20035

            JVM lang
            > http://habr.com/post91005

            Go and performance
            > http://habr.com/post13455

            Java annotation
            > http://habr.com/post11356

            Kotlin inline function
            > http://habr.com/post31456
        """.trimIndent()
        mockWebClient.webhookPostReceiveMessage("/posts_random", "John", 1000L)
        verify(chatManager, times(1)).sendMessage(1000L, text = expected1, preview = false)

        mockWebClient.webhookPostReceiveMessage("/posts_random", "Alex", 2000L)
        verify(chatManager, times(1)).sendMessage(2000L, text = expected2, preview = false)

        mockWebClient.webhookPostReceiveMessage("Send me 5 posts", "Alex", 2000L)
        verify(chatManager, times(1)).sendMessage(2000L, text = expected3, preview = false)
    }

    @Test fun onRandomPostByQuery_shouldSendValidMessage() {
        val query1 = "Python"
        val query2 = "Ruby"
        `when`(postService.getPostByQuery(query1)).thenReturn(
                Single.just(Post("Python. Best practices", "http://habr.com/post20035")),
                Single.just(Post("Python for beginners", "http://habr.com/post12005"))
        )
        `when`(postService.getPostByQuery(query2)).thenReturn(
                Single.just(Post("Ruby. Best practices", "http://habr.com/post20035")),
                Single.just(Post("Ruby for beginners", "http://habr.com/post12005"))
        )
        mockWebClient.webhookPostReceiveMessage("Send me \\\"Python\\\" post", "John", 1000L)
        verify(chatManager, times(1)).sendMessage(1000L, "> http://habr.com/post20035", preview = true)
        mockWebClient.webhookPostReceiveMessage("Send me \\\"Python\\\" post", "Alex", 2000L)
        verify(chatManager, times(1)).sendMessage(2000L, "> http://habr.com/post12005", preview = true)

        mockWebClient.webhookPostReceiveMessage("Send me \\\"Ruby\\\" post", "Emma", 3000L)
        verify(chatManager, times(1)).sendMessage(3000L, "> http://habr.com/post20035", preview = true)
        mockWebClient.webhookPostReceiveMessage("Send me \\\"Ruby\\\" post", "Nick", 4000L)
        verify(chatManager, times(1)).sendMessage(4000L, "> http://habr.com/post12005", preview = true)
    }

    @Test fun onPostsByQuery_shouldSendValidMessage() {
        val query1 = "Python"
        val query2 = "Ruby"
        `when`(postService.getPostsByQuery(query1, 2)).thenReturn(
                Single.just(listOf(
                        Post("Python. Best practices", "http://habr.com/post20035"),
                        Post("Python for beginners", "http://habr.com/post12005"))
                )
        )
        `when`(postService.getPostsByQuery(query2)).thenReturn(
                Single.just(listOf(
                        Post("Ruby. Best practices", "http://habr.com/post20035"),
                        Post("Ruby for beginners", "http://habr.com/post12005"),
                        Post("Ruby and RoR framework", "http://habr.com/post113567"))
                )
        )
        val expected1 = """
            Python. Best practices
            > http://habr.com/post20035

            Python for beginners
            > http://habr.com/post12005
        """.trimIndent()
        val expected2 = """
            Ruby. Best practices
            > http://habr.com/post20035

            Ruby for beginners
            > http://habr.com/post12005

            Ruby and RoR framework
            > http://habr.com/post113567
        """.trimIndent()
        mockWebClient.webhookPostReceiveMessage("Send me 2 \\\"Python\\\" posts", "John", 1000L)
        verify(chatManager, times(1)).sendMessage(1000L, text = expected1, preview = false)
        mockWebClient.webhookPostReceiveMessage("Send me \\\"Ruby\\\" posts", "Alex", 2000L)
        verify(chatManager, times(1)).sendMessage(2000L, text = expected2, preview = false)
    }
}