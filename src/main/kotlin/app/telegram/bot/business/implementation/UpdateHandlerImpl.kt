package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import com.pengrad.telegrambot.model.Update
import org.springframework.beans.factory.annotation.Autowired

class UpdateHandlerImpl(@Autowired private val botInteractor: BotInteractor) : UpdateHandler {
    override fun onUpdateReceive(update: Update) {
        val text = update.message().text()
        when(text) {
            "/weather/current" -> botInteractor.sendCurrentWeather()
            "/weather/today" -> botInteractor.sendTodayWeather()
            "/weather/tomorrow" -> botInteractor.sendTomorrowWeather()
            "/weather/week" -> botInteractor.sendWeekWeather()

            "/post/random" -> botInteractor.sendRandomPost()
            "/posts/random" -> botInteractor.sendRandomPosts()
//            "/posts/random?count=" -> botInteractor.sendRandomPosts(10)
//            "/posts/keywords?q=" -> botInteractor.sendPostsByKeywords("a", "b")
//            "/posts/keywords/?q=%s&count=%s" -> botInteractor.sendPostsByKeywords("a", "b")
        }
    }
}