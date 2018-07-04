package app.telegram.bot.business.inheritence

interface BotInteractor {
    fun sendCurrentWeather(chatId: Long)
    fun sendTodayWeather(chatId: Long)
    fun sendTomorrowWeather(chatId: Long)
    fun sendWeekWeather(chatId: Long)

    fun sendRandomPost(chatId: Long)
    fun sendRandomPosts(chatId: Long, count: Int = 4)
    fun sendPostsByKeywords(chatId: Long, vararg keywords: String, count: Int = 4)
}