package app.telegram.bot.business.error

class NoPostsFound(query: String = "") : Throwable("Can't find posts" + if (query.isNotEmpty()) " by query['$query']" else "")