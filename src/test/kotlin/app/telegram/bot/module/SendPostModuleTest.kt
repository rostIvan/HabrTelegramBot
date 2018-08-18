package app.telegram.bot.module

import app.telegram.bot.Config
import app.telegram.bot.api.habr.PostApi
import app.telegram.bot.api.habr.PostParseWrapper
import app.telegram.bot.business.inheritence.ChatManager
import io.reactivex.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
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
class SendPostModuleTest {
    @Autowired lateinit var webClient: WebTestClient
    @MockBean lateinit var postApi: PostApi
    @MockBean lateinit var chatManager: ChatManager
    val chatId: Long = 1000413156L

    @Test fun onRandomPostQuery_shouldSendValidMessage() {
        `when`(postApi.getRandomPost()).thenReturn(randomPostSingle())
        ModuleTestUtil(webClient).webhookPostReceiveMessage("/post_random")
        val expect = "Анализ графиков бинарных опционов или как я в очередной раз доказал себе, что халявы не существует >>> " +
                "<a href=\"https://habr.com/post/420523/\">Link</a>"
        verify(chatManager, times(1)).sendMessage(chatId = chatId, text = expect, preview = true)
    }

    @Test fun onRandomPostsQuery_shouldSendValidMessage() {
        `when`(postApi.getRandomPosts(anyInt())).thenReturn(randomPostsSingle())
        ModuleTestUtil(webClient).webhookPostReceiveMessage("/posts_random")
        val expect = """
            Сети для самых матёрых. Часть пятнадцатая. QoS >>> <a href="https://habr.com/post/420525/">Link</a>

            Работа IT-специалистом на Дальнем Востоке — Амурская область >>> <a href="https://habr.com/post/420526/">Link</a>
        """.trimIndent()
        verify(chatManager, times(1)).sendMessage(chatId = chatId, text = expect, preview = false)
    }

    private fun randomPostsSingle(): Single<List<PostParseWrapper>> = Single.just(listOf(
            PostParseWrapper("Сети для самых матёрых. Часть пятнадцатая. QoS", "https://habr.com/post/420525/",
                    "СДСМ-15. Про QoS.\nТеперь с возможностью Pull Request'ов.",
                    listOf("Стандарты связи", "Системное администрирование", "Сетевые технологии"),
                    "eucariot","сегодня в 13:14"),
            PostParseWrapper("Работа IT-специалистом на Дальнем Востоке — Амурская область", "https://habr.com/post/420526/",
                    "Прочитав Стоит ли ехать It-специалисту на Дальний Восток я в комментариях обещал рассказать про свою область. В данной публикации я опишу опыт поиска работы со стороны Junior’a, да и про жизнь в области в целом. Историю дополню своим жизненным опытом, так что надеюсь вам понравится.",
                    listOf("Учебный процесс в IT", "Карьера в IT-индустрии", "IT-эмиграция"),
                    "Bizonozubr", "сегодня в 10:47"))
    )

    private fun randomPostSingle() = Single.just(PostParseWrapper(
            title = "Анализ графиков бинарных опционов или как я в очередной раз доказал себе, что халявы не существует",
            link = "https://habr.com/post/420523/",
            date = "сегодня в 10:23",
            tags = listOf("Финансы в IT", "Программирование", "Научно-популярное"),
            postByUser = "DeuS7",
            description = "Недавно я наткнулся на занимательное видео из разряда «Чтобы быстро стать богатым нужно всего лишь...». Видео начинается пафосным пересчитыванием солидной пачки денег и демонстрированием приличного счета. Далее парень показывает стратегию, которая основана на фразе «Ну вот смотрите на график, тут видно» \n\n Однако я человек скромный, а потому решил прежде чем пойти за своими лярдами сначала проверить данную стратегию математически и программно. Ниже вы можете посмотреть, что из этого вышло."
    ))
}