package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Update
import org.springframework.beans.factory.annotation.Autowired

class UpdateHandlerImpl(private val currentUser: CurrentUser,
                        private val botInteractor: BotInteractor) : UpdateHandler {

    override fun onUpdateReceive(update: Update) {
        val message = update.message()
        updateChat(message.chat())
        processCommands(message.text())
//        processPhrases()
    }

    private fun updateChat(message: Chat) {
        val user = ChatUser(chatId = message.id(), nickname = message.username())
        currentUser.update(user)
    }

    private fun processCommands(text: String) {
        when (text) {
            "/start" -> botInteractor.sendHello()
            in listOf("/help", "--help", "-h") -> botInteractor.sendHelp()
            "/weather_current" -> botInteractor.sendCurrentWeather()
            "/weather_today" -> botInteractor.sendTodayWeather()
            "/weather_tomorrow" -> botInteractor.sendTomorrowWeather()
            "/weather_week" -> botInteractor.sendWeekWeather()
            "/post_random" -> botInteractor.sendRandomPost()
            "/posts_random" -> botInteractor.sendRandomPosts()
//            "/post_relevant" -> {  }
//            "/posts_relevant " -> {  }
        }
    }

//    private fun processPhrases(text: String, chatId: Long) {
//        if (text.containsIgnoreCase("Send me")) {
//            if (text.containsIgnoreCase("post")) {  }
//            else if (text.containsIgnoreCase("posts")) {  }
//        }
//    }
}