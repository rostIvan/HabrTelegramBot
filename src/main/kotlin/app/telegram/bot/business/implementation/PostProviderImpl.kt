package app.telegram.bot.business.implementation

import app.telegram.bot.api.habr.PostApi
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.data.Post
import io.reactivex.Single

class PostProviderImpl(postApi: PostApi) : PostProvider {
    override fun getRandomPost(): Single<Post> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRandomPosts(count: Int): Single<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPostsByKeywords(vararg words: String, count: Int): Single<List<Post>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}