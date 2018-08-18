package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.ChatManager
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.request.SendMessage
import org.springframework.beans.factory.annotation.Autowired

class ChatManagerImpl(@Autowired private val telegramBot: TelegramBot) : ChatManager {

    override fun sendMessage(chatId: Long, text: String, preview: Boolean) {
        val sendMessage = SendMessage(chatId, text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(!preview)
        telegramBot.execute(sendMessage)
    }

}