package app.telegram.bot.api.habr

import io.reactivex.Single

interface PostApi {
    fun getRandomPost(): Single<PostParseWrapper>
    fun getPostByQuery(query: String) : Single<PostParseWrapper>
    fun getRandomPosts(count: Int = 10): Single<List<PostParseWrapper>>
    fun getPostsByQuery(query: String, count: Int = 10): Single<List<PostParseWrapper>>
}