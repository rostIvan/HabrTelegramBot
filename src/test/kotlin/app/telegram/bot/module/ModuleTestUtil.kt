package app.telegram.bot.module

import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

class ModuleTestUtil(private val webClient: WebTestClient) {
    fun webhookPostReceiveMessage(message: String) {
        webClient.post()
                .uri("/webhook")
                .body(BodyInserters.fromObject(mockUpdateBody(message)))
                .exchange()
                .expectStatus().isOk
    }

    fun mockUpdateBody(textMessage: String) : String {
        return javaClass.getResource("/update-example.json")
                .readText()
                .replace("""text": "/start""", """text": "$textMessage""")
    }
}
