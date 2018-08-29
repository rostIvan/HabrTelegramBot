package app.telegram.bot.data.model

data class PostDTO(var title: String,
                   var link: String,
                   var description: String,
                   var tags: List<String>,
                   var postByUser: String,
                   var date: String)