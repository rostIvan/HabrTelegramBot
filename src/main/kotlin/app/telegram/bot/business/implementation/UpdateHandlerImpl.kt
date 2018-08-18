package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import com.pengrad.telegrambot.model.Update
import org.springframework.beans.factory.annotation.Autowired

class UpdateHandlerImpl(@Autowired private val botInteractor: BotInteractor) : UpdateHandler {
    override fun onUpdateReceive(update: Update) {
        val message = update.message()
        val chatId = message.chat().id()
        val text = message.text()
        when (text) {
            "/start" -> botInteractor.sendHello(chatId)
            in listOf("/help", "--help", "-h") -> botInteractor.sendHelp(chatId)
            "/weather_current" -> botInteractor.sendCurrentWeather(chatId)
            "/weather_today" -> botInteractor.sendTodayWeather(chatId)
            "/weather_tomorrow" -> botInteractor.sendTomorrowWeather(chatId)
            "/weather_week" -> botInteractor.sendWeekWeather(chatId)
            "/post_random" -> botInteractor.sendRandomPost(chatId)
            "/posts_random" -> botInteractor.sendRandomPosts(chatId)
        }
    }
}