package app.telegram.bot.util

import app.telegram.bot.api.habr.PostParseWrapper
import app.telegram.bot.data.Post

object PostUtil {
    fun toPost(postParseWrapper: PostParseWrapper) : Post = Post(
            title = postParseWrapper.title,
            link = postParseWrapper.link
    )

    fun toPosts(postsWrapper: List<PostParseWrapper>) : List<Post> = postsWrapper.stream().map(::toPost).collectAsList()
}