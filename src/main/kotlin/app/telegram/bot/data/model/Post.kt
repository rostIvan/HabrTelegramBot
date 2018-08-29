package app.telegram.bot.data.model

import app.telegram.bot.util.formatPost

data class Post(var title: String, var link: String) : MessageText {

    override fun toMessage(): String = formatPost(this)

}
