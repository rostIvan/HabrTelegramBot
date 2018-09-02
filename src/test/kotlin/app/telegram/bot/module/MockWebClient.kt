package app.telegram.bot.module

import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

class MockWebClient(private val webClient: WebTestClient) {
    fun webhookPostReceiveMessage(message: String, username: String = "", chatId: Long = 1000413156) {
        webClient.post()
                .uri("/webhook")
                .body(BodyInserters.fromObject(mockUpdateBody(message, username, chatId)))
                .exchange()
                .expectStatus().isOk
    }
//    "id": 1000413156
    fun mockUpdateBody(textMessage: String, username: String, chatId: Long) : String {
        val updateJson = javaClass.getResource("/update-example.json").readText()
        val replacedMessage = updateJson
                .replace("\"text\": \"/start\"", "\"text\": \"$textMessage\"")
                .replace("\"id\": 1000413156", "\"id\": $chatId")
        return if (username.isNotBlank())
            replacedMessage.replace("\"username\": \"jiayu\"", "\"username\": \"$username\"")
        else replacedMessage
    }
}
