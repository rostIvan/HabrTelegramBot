package app.telegram.bot.business.inheritence

import app.telegram.bot.data.Post
import io.reactivex.Observable

interface PostProvider {
    fun getRandomPost() : Observable<Post>
    fun getRandomPosts(count: Int = 4) : Observable<List<Post>>
    fun getPostsByKeywords(vararg words: String, count: Int = 4) : Observable<List<Post>>
}