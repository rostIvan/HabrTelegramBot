package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.AbstractMessagePreparation
import app.telegram.bot.data.Post
import app.telegram.bot.data.Weather

class MessagePreparation {
    fun post() = object : AbstractMessagePreparation<Post>() {
        override fun toMessage(t: Post): String { TODO() }
        override fun toMessage(list: List<Post>): String { TODO()  }
    }

    fun weather() = object : AbstractMessagePreparation<Weather>() {
        override fun toMessage(t: Weather): String { TODO() }
        override fun toMessage(list: List<Weather>): String { TODO() }
    }
}