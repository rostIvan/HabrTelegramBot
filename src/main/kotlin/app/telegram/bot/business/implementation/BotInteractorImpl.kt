package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.business.inheritence.WeatherProvider
import app.telegram.bot.data.Weather
import com.pengrad.telegrambot.TelegramBot
import io.reactivex.functions.BiConsumer
import org.springframework.beans.factory.annotation.Autowired

class BotInteractorImpl(@Autowired private val chatManager: ChatManager,
                        @Autowired private val weatherProvider: WeatherProvider,
                        @Autowired private val postProvider: PostProvider) : BotInteractor {
    override fun sendCurrentWeather(chatId: Long) {
        val currentWeather = weatherProvider.getCurrentWeather()
        currentWeather.subscribe(
                { current -> chatManager.sendWeather(chatId = chatId, weather = current, type = Weather.Type.CURRENT) },
                { error -> handleError(chatId, error) }
        )
    }

    override fun sendTodayWeather(chatId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendTomorrowWeather(chatId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendWeekWeather(chatId: Long) {
        val weekWeather = weatherProvider.getWeekWeather()
        weekWeather.subscribe(
                { week ->  chatManager.sendWeatherForecast(chatId = chatId, forecast = week) },
                {error -> handleError(chatId, error) }
        )
    }

    override fun sendRandomPost(chatId: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendRandomPosts(chatId: Long, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendPostsByKeywords(chatId: Long, vararg keywords: String, count: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun handleError(chatId: Long, error: Throwable) {
        chatManager.sendMessage(chatId = chatId, text = "Sorry, but occurred some error: ${error.message}")
    }
}