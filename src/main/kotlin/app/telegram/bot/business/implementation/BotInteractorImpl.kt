package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.business.inheritence.WeatherProvider
import app.telegram.bot.data.Weather
import app.telegram.bot.util.getHelpMessage
import app.telegram.bot.util.getStartMessage
import app.telegram.bot.util.toMessage
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired

class BotInteractorImpl(@Autowired private val chatManager: ChatManager,
                        @Autowired private val weatherProvider: WeatherProvider,
                        @Autowired private val postProvider: PostProvider) : BotInteractor {
    override fun sendHello(chatId: Long) {
        chatManager.sendMessage(chatId, getStartMessage())
    }

    override fun sendHelp(chatId: Long) {
        chatManager.sendMessage(chatId, getHelpMessage())
    }

    override fun sendCurrentWeather(chatId: Long) {
        val currentWeather = weatherProvider.getCurrentWeather()
        sendWeatherMessage(chatId, currentWeather)
    }

    override fun sendTodayWeather(chatId: Long) {
        val todayWeather = weatherProvider.getTodayWeather()
        sendWeatherMessage(chatId, todayWeather)
    }

    override fun sendTomorrowWeather(chatId: Long) {
        val tomorrowWeather = weatherProvider.getTomorrowWeather()
        sendWeatherMessage(chatId, tomorrowWeather)
    }

    override fun sendWeekWeather(chatId: Long) {
        val weekWeather = weatherProvider.getWeekWeather()
        weekWeather.subscribe(
                { week ->  chatManager.sendMessage(chatId = chatId, text = week.toMessage()) },
                { error -> handleError(chatId, error) }
        )
    }

    override fun sendRandomPost(chatId: Long) {
        val randomPost = postProvider.getRandomPost()
        randomPost.subscribe(
            { post -> chatManager.sendMessage(chatId = chatId, text = post.toMessage(), preview = true ) },
            { error -> handleError(chatId, error) }
        )
    }

    override fun sendRandomPosts(chatId: Long, count: Int) {
        val randomPosts = postProvider.getRandomPosts(4)
        randomPosts.subscribe(
                { posts -> chatManager.sendMessage(chatId = chatId, text = posts.toMessage(), preview = false ) },
                { error -> handleError(chatId, error) }
        )
    }

    private fun sendWeatherMessage(chatId: Long, weatherResponse: Single<Weather>) {
        weatherResponse.subscribe(
                { weather -> chatManager.sendMessage(chatId = chatId, text = weather.toMessage()) },
                { error -> handleError(chatId, error) }
        )
    }

    private fun handleError(chatId: Long, error: Throwable) {
        chatManager.sendMessage(chatId = chatId, text = "Sorry, but occurred some error: ${error.message}")
    }
}