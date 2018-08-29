package app.telegram.bot.module

import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

class MockWebClient(private val webClient: WebTestClient) {
    fun webhookPostReceiveMessage(message: String, username: String = "") {
        webClient.post()
                .uri("/webhook")
                .body(BodyInserters.fromObject(mockUpdateBody(message, username)))
                .exchange()
                .expectStatus().isOk
    }

    fun mockUpdateBody(textMessage: String, username: String) : String {
        val updateJson = javaClass.getResource("/update-example.json").readText()
        val replacedMessage = updateJson.replace("""text": "/start""", """text": "$textMessage""")
        return if (username.isNotBlank()) replacedMessage.replace("""username": "jiayu""", """username": "$username""")
        else replacedMessage
    }
}
