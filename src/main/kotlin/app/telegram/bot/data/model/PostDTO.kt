package app.telegram.bot.data.model

import app.telegram.bot.data.storage.hibernate.PostDbModel

data class PostDTO(var title: String,
                   var link: String,
                   var description: String,
                   var tags: List<String>,
                   var postByUser: String,
                   var date: String) {

    fun toDb() = PostDbModel(title = title, link = link, description = description, tags = tags, postByUser = postByUser, date = date)
}