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
        when(text) {
            "/weather/current" -> botInteractor.sendCurrentWeather(chatId)
            "/weather/today" -> botInteractor.sendTodayWeather(chatId)
            "/weather/tomorrow" -> botInteractor.sendTomorrowWeather(chatId)
            "/weather/week" -> botInteractor.sendWeekWeather(chatId)

//            "/post/random" -> botInteractor.sendRandomPost(chatId)
//            "/posts/random" -> botInteractor.sendRandomPosts(chatId)
//            "/posts/random?count=" -> botInteractor.sendRandomPosts(chatId, 10)
//            "/posts/keywords?q=" -> botInteractor.sendPostsByKeywords(0, *arrayOf("", "123"))
//            "/posts/keywords/?q=%s&count=%s" -> botInteractor.sendPostsByKeywords("a", "b")
        }
    }
}