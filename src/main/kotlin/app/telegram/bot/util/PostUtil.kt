package app.telegram.bot.util

import app.telegram.bot.data.model.Post
import app.telegram.bot.data.model.PostDTO

object PostUtil {
    fun toPost(postParseWrapper: PostDTO) : Post = Post(
            title = postParseWrapper.title,
            link = postParseWrapper.link
    )

    fun toPosts(postsWrapper: List<PostDTO>) : List<Post> = postsWrapper.map(::toPost)
}