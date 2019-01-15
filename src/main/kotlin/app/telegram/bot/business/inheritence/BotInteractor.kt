package app.telegram.bot.business.inheritence

interface BotInteractor {
    fun sendHello()
    fun sendHelp()

    fun sendRandomPost()
    fun sendPostByQuery(query: String)
    fun sendRandomPosts(count: Int = 3)
    fun sendPostsByQuery(query: String, count: Int = 3)
}