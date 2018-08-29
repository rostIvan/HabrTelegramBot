package app.telegram.bot.data.service.post

import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.Post
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.storage.dao.DaoStorage
import app.telegram.bot.util.PostUtil.toPost
import app.telegram.bot.util.PostUtil.toPosts
import io.reactivex.Single
import org.springframework.stereotype.Service

@Service
class PostServiceImpl(private val postApi: PostApi,
                      private val storage: DaoStorage<PostDTO>) : PostService {

    override fun getRandomPost(): Single<Post> = postApi
            .getRandomPost(exclude = findAllThatAlreadySent())
            .doOnSuccess { post -> save(post) }
            .map(::toPost)

    override fun getPostByQuery(query: String): Single<Post> = postApi
            .getPostByQuery(query, exclude = findAllThatAlreadySent())
            .doOnSuccess { post -> save(post) }
            .map(::toPost)

    override fun getRandomPosts(count: Int): Single<List<Post>> = postApi
            .getRandomPosts(count, exclude = findAllThatAlreadySent())
            .doOnSuccess { posts -> saveAll(posts) }
            .map(::toPosts)

    override fun getPostsByQuery(query: String, count: Int): Single<List<Post>> = postApi
            .getPostsByQuery(query, count, exclude = findAllThatAlreadySent())
            .doOnSuccess { posts -> saveAll(posts) }
            .map(::toPosts)

    private fun findAllThatAlreadySent() = storage.findAll()
    private fun save(post: PostDTO) { storage.save(post) }
    private fun saveAll(posts: Iterable<PostDTO>) { storage.saveAll(posts) }
}