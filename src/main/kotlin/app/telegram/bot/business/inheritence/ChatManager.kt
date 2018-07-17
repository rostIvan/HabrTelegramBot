package app.telegram.bot.business.inheritence

interface ChatManager {
    fun sendMessage(chatId: Long, text: String)
}