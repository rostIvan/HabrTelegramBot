package app.telegram.bot.business.error

class NoPostsFound(query: String = "") : Throwable("Can't find posts" + if (query.isNotEmpty()) " by query['$query']" else "")
class PostRangeError(count: Int) : Throwable("Number of posts should be in range[1, 80] but you choose $count")