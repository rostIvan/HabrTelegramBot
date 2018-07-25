package app.telegram.bot.api.habr

data class PostParseWrapper(var title: String,
                            var link: String,
                            var description: String,
                            var tags: List<String>,
                            var postByUser: String,
                            var date: String)