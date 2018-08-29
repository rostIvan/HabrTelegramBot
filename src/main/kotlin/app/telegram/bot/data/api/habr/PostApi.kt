package app.telegram.bot.data.api.habr

import app.telegram.bot.data.model.PostDTO
import io.reactivex.Single

interface PostApi {
    fun getRandomPost(exclude: List<PostDTO> = emptyList()): Single<PostDTO>

    fun getPostByQuery(query: String,
                       exclude: List<PostDTO> = emptyList()) : Single<PostDTO>

    fun getRandomPosts(count: Int = 5,
                       exclude: List<PostDTO> = emptyList()): Single<List<PostDTO>>

    fun getPostsByQuery(query: String,
                        count: Int = 5,
                        exclude: List<PostDTO> = emptyList()): Single<List<PostDTO>>
}