package app.telegram.bot.data.service.post

import app.telegram.bot.data.model.Post
import io.reactivex.Single

interface PostService {
    fun getRandomPost() : Single<Post>
    fun getPostByQuery(query: String) : Single<Post>
    fun getRandomPosts(count: Int = 3) : Single<List<Post>>
    fun getPostsByQuery(query: String, count: Int = 3) : Single<List<Post>>
}