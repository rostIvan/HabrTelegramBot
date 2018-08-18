package app.telegram.bot.business.implementation

import app.telegram.bot.api.habr.PostApi
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.data.Post
import app.telegram.bot.util.PostUtil
import io.reactivex.Single

class PostProviderImpl(private val postApi: PostApi) : PostProvider {

    override fun getRandomPost(): Single<Post> = postApi.getRandomPost().map(PostUtil::toPost)

    override fun getRandomPosts(count: Int): Single<List<Post>> = postApi.getRandomPosts(count).map(PostUtil::toPosts)

}