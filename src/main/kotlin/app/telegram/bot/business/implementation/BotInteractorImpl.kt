package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Autowired

class BotInteractorImpl(@Autowired telegramBot: TelegramBot) : BotInteractor {
    override fun sendCurrentWeather() { TODO() }
    override fun sendTodayWeather() { TODO() }
    override fun sendTomorrowWeather() { TODO() }
    override fun sendWeekWeather() { TODO() }

    override fun sendRandomPost() { TODO() }
    override fun sendRandomPosts(count: Int) { TODO() }
    override fun sendPostsByKeywords(vararg keywords: String, count: Int) { TODO() }
}