package app.telegram.bot.business.inheritence

interface BotInteractor {
    fun sendCurrentWeather()
    fun sendTodayWeather()
    fun sendTomorrowWeather()
    fun sendWeekWeather()

    fun sendRandomPost()
    fun sendRandomPosts(count: Int = 4)
    fun sendPostsByKeywords(vararg keywords: String, count: Int = 4)
}