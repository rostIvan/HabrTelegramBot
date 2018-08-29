package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.model.MessageText
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.util.getHelpMessage
import app.telegram.bot.util.getStartMessage
import app.telegram.bot.util.toMessage
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class BotInteractorImpl (private val user: CurrentUser,
                         private val chatManager: ChatManager,
                         private val weatherService: WeatherService,
                         private val postService: PostService) : BotInteractor {

    // Basic
    override fun sendHello() {
        chatManager.sendMessage(user.chatId(), text = getStartMessage(user.nickname()))
    }

    override fun sendHelp() {
        chatManager.sendMessage(user.chatId(), text = getHelpMessage())
    }

    // Weather
    override fun sendCurrentWeather() {
        val currentWeather = weatherService.getCurrentWeather()
        sendMessage(currentWeather)
    }

    override fun sendTodayWeather() {
        val todayWeather = weatherService.getTodayWeather()
        sendMessage(todayWeather)
    }

    override fun sendTomorrowWeather() {
        val tomorrowWeather = weatherService.getTomorrowWeather()
        sendMessage(tomorrowWeather)
    }

    override fun sendWeekWeather() {
        val weekWeather = weatherService.getWeekWeather()
        sendMessage(weekWeather)
    }

    // Posts
    override fun sendRandomPost() {
        val randomPost = postService.getRandomPost()
        sendMessage(randomPost, preview = true)
    }

    override fun sendPostByQuery(query: String) {
        val postByQuery = postService.getPostByQuery(query)
        sendMessage(postByQuery, preview = true)
    }

    override fun sendRandomPosts(count: Int) {
        val randomPosts = postService.getRandomPosts(count)
        sendMessage(randomPosts)
    }

    override fun sendPostsByQuery(query: String, count: Int) {
        val postsByQuery = postService.getPostsByQuery(query, count)
        sendMessage(postsByQuery)
    }

    private inline fun <reified T : MessageText> sendMessage(response: Single<T>, preview: Boolean = false) {
        response
                .subscribeOn(Schedulers.io())
                .subscribe (
                    { text -> chatManager.sendMessage(chatId = user.chatId(), text = text.toMessage(), preview = preview ) },
                    { error -> handleError(user.chatId(), error) }
                )
    }

    private inline fun <reified T : MessageText> sendMessage(response: Single<List<T>>) {
        response
                .subscribeOn(Schedulers.io())
                .subscribe (
                    { text -> chatManager.sendMessage(chatId = user.chatId(), text = text.toMessage(), preview = false) },
                    { error -> handleError(user.chatId(), error) }
                )
    }

    private fun handleError(chatId: Long, error: Throwable) {
        chatManager.sendMessage(chatId = chatId, text = "Sorry, but occurred some error: ${error.message}")
    }
}