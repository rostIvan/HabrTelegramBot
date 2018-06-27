package app.telegram.bot.controller

import com.pengrad.telegrambot.BotUtils
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BotController(@Autowired val telegramBot: TelegramBot) {

    @PostMapping("/webhook")
    fun webhook(@RequestBody updateBody: String) {
        val update = BotUtils.parseUpdate(updateBody)
        val message = update.message()
        val chat = message.chat()
        when(message.text()) {
            "/start" -> sendMessage(chatId = chat.id(), text = "Hello! Let's start")
            "/command1" -> sendMessage(chatId = chat.id(), text = "Hello! 1 command")
            "/command2" -> sendMessage(chatId = chat.id(), text = "Hello! 2 command")
            "Hello" -> sendMessage(chatId = chat.id(), text = "Hello!")
            "Hi" -> sendMessage(chatId = chat.id(), text = "Hi! How are you?")
        }
    }

    fun sendMessage(chatId: Long, text: String) {
        val sendMessage = SendMessage(chatId, text)
        telegramBot.execute(sendMessage)
    }
}