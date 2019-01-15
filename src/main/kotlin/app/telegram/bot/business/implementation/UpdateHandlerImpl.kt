package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.UpdateHandler
import app.telegram.bot.data.model.ChatUser
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.util.extractCount
import app.telegram.bot.util.extractQuery
import com.pengrad.telegrambot.model.Chat
import com.pengrad.telegrambot.model.Update
import java.util.regex.Pattern

class UpdateHandlerImpl(private val currentUser: CurrentUser,
                        private val botInteractor: BotInteractor) : UpdateHandler {

    override fun onUpdateReceive(update: Update) {
        val message = if (update.message() != null) update.message() else update.editedMessage()
        updateChat(message.chat())
        processCommands(message.text())
        processPhrases(message.text())
    }

    private fun updateChat(message: Chat) {
        val user = ChatUser(chatId = message.id(), nickname = message.username())
        currentUser.update(user)
    }

    private fun processCommands(text: String) {
        when (text) {
            "/start" -> botInteractor.sendHello()
            in listOf("/help", "--help", "-h") -> botInteractor.sendHelp()
            "/post_random" -> botInteractor.sendRandomPost()
            "/posts_random" -> botInteractor.sendRandomPosts()
//            "/post_relevant" -> {  }
//            "/posts_relevant " -> {  }
        }
    }

    private fun processPhrases(text: String) {
        val pattern1 = Pattern.compile("Send me \"(\\w+)\" post", Pattern.CASE_INSENSITIVE).matcher(text)
        val pattern2 = Pattern.compile("Send me \"(\\w+)\" posts", Pattern.CASE_INSENSITIVE).matcher(text)
        val pattern3 = Pattern.compile("Send me (\\d+) posts", Pattern.CASE_INSENSITIVE).matcher(text)
        val pattern4 = Pattern.compile("Send me (\\d+) \"(\\w+)\" posts", Pattern.CASE_INSENSITIVE).matcher(text)

        when {
            pattern1.matches() -> botInteractor.sendPostByQuery(extractQuery(pattern1))
            pattern2.matches() -> botInteractor.sendPostsByQuery(extractQuery(pattern2))
            pattern3.matches() -> botInteractor.sendRandomPosts(extractCount(pattern3))
            pattern4.matches() -> {
                val count = pattern4.group(1).toInt()
                val query = pattern4.group(2).toString()
                botInteractor.sendPostsByQuery(query, count)
            }
        }
    }
}