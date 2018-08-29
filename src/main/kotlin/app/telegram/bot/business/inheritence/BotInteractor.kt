package app.telegram.bot.business.inheritence

import app.telegram.bot.data.model.ChatUser

interface BotInteractor {
    fun sendHello()
    fun sendHelp()

    fun sendCurrentWeather()
    fun sendTodayWeather()
    fun sendTomorrowWeather()
    fun sendWeekWeather()

    fun sendRandomPost()
    fun sendPostByQuery(query: String)
    fun sendRandomPosts(count: Int = 3)
    fun sendPostsByQuery(query: String, count: Int = 3)
}