package app.telegram.bot.data.model

open class CurrentUser {
    private lateinit var chatUser: ChatUser
    fun update(chatUser: ChatUser) { this.chatUser = chatUser }
    fun chatId() = chatUser.chatId
    fun nickname() = chatUser.nickname
}