package app.telegram.bot

import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Config {
    @Value("\${habr.base-url}") lateinit var habrUrl: String
    @Value("\${medium.base-url}") lateinit var mediumUrl: String
    @Value("\${andropub.base-url}") lateinit var androidPubUrl: String
    @Value("\${bot.api-key}") lateinit var accessToken: String

    @Bean
    fun bot() = TelegramBot(accessToken)
}