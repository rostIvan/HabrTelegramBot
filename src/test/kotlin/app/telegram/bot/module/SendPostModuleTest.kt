package app.telegram.bot.module

import app.telegram.bot.Config
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.storage.dao.DaoStorage
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
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
//@Ignore
class SendPostModuleTest {
    @Autowired lateinit var webClient: WebTestClient
    @MockBean lateinit var postApi: PostApi
    @MockBean lateinit var chatManager: ChatManager
    lateinit var mockWebClient: MockWebClient

    @Before fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        mockWebClient = MockWebClient(webClient)
    }

    @Test fun onRandomPostQuery_shouldSendValidMessage() {
        mockWebClient.webhookPostReceiveMessage("/start", "John")
        println()
        mockWebClient.webhookPostReceiveMessage("/start", "Ivan")
        println()
    }

}