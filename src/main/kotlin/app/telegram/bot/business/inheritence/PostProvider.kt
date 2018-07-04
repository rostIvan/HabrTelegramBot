package app.telegram.bot.business.inheritence

import app.telegram.bot.data.Post
import io.reactivex.Single

interface PostProvider {
    fun getRandomPost() : Single<Post>
    fun getRandomPosts(count: Int = 4) : Single<List<Post>>
    fun getPostsByKeywords(vararg words: String, count: Int = 4) : Single<List<Post>>
}