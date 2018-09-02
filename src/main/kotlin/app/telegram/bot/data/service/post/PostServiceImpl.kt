package app.telegram.bot.data.service.post

import app.telegram.bot.business.error.PostRangeError
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.Post
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.storage.dao.DaoStorage
import app.telegram.bot.data.storage.hibernate.PostDbModel
import app.telegram.bot.util.PostUtil.toPost
import app.telegram.bot.util.PostUtil.toPosts
import io.reactivex.Single
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(private val postApi: PostApi,
                      private val storage: DaoStorage<PostDbModel>) : PostService {

    override fun getRandomPost(): Single<Post> = processPost { getRandomPost(exclude = findAllThatAlreadySent()) }

    override fun getPostByQuery(query: String): Single<Post> = processPost { getPostByQuery(query, exclude = findAllThatAlreadySent()) }

    override fun getRandomPosts(count: Int): Single<List<Post>> = processPosts(count) {
        getRandomPosts(count, exclude = findAllThatAlreadySent())
    }

    override fun getPostsByQuery(query: String, count: Int): Single<List<Post>> = processPosts(count) {
        getPostsByQuery(query, count, exclude = findAllThatAlreadySent())
    }

    private fun processPost(apiFun: PostApi.() -> Single<PostDTO>) = apiFun.invoke(postApi)
            .doOnSuccess { post -> save(post) }
            .map(::toPost)

    private fun processPosts(count: Int, apiFun: PostApi.() -> Single<List<PostDTO>>) =
        if (count in (1..80))
                apiFun.invoke(postApi)
                        .doOnSuccess { posts -> saveAll(posts) }
                        .map(::toPosts)
        else Single.error(PostRangeError(count))

    private fun findAllThatAlreadySent() = storage.findAll().map { it.toDTO() }
    private fun save(post: PostDTO) { storage.save(post.toDb()) }
    private fun saveAll(posts: Iterable<PostDTO>) { storage.saveAll(posts.map { it.toDb() }) }
}