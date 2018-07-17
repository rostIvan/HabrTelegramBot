package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import com.pengrad.telegrambot.model.Update
import org.springframework.beans.factory.annotation.Autowired

/**
 *   weather_current -> Send current weather
 *   weather_today -> Send today weather
 *   weather_tomorrow -> Send tomorrow weather
 *   weather_week -> Send week weather
 **/
class UpdateHandlerImpl(@Autowired private val botInteractor: BotInteractor) : UpdateHandler {
    override fun onUpdateReceive(update: Update) {
        val message = update.message()
        val chatId = message.chat().id()
        val text = message.text()
        when(text) {
            "/start" -> botInteractor.sendHello(chatId)
            "/weather_current" -> botInteractor.sendCurrentWeather(chatId)
            "/weather_today" -> botInteractor.sendTodayWeather(chatId)
            "/weather_tomorrow" -> botInteractor.sendTomorrowWeather(chatId)
            "/weather_week" -> botInteractor.sendWeekWeather(chatId)
//            "/post/random" -> botInteractor.sendRandomPost(chatId)
//            "/posts/random" -> botInteractor.sendRandomPosts(chatId)
//            "/posts/random?count=" -> botInteractor.sendRandomPosts(chatId, 10)
//            "/posts/keywords?q=" -> botInteractor.sendPostsByKeywords(0, *arrayOf("", "123"))
//            "/posts/keywords/?q=%s&count=%s" -> botInteractor.sendPostsByKeywords("a", "b")
        }
    }
}