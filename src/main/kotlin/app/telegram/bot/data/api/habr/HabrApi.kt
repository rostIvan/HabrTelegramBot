package app.telegram.bot.data.api.habr

import app.telegram.bot.business.error.NoPostsFound
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.util.random
import io.reactivex.Single
import org.jsoup.HttpStatusException
import org.springframework.beans.factory.annotation.Autowired

class HabrApi(@Autowired private val site: HabrSiteParser) : PostApi {

    override fun getRandomPost(exclude: List<PostDTO>): Single<PostDTO> {
        return Single
                .fromCallable { getPostItem(exclude = exclude) }
                .resumeErrors()
    }

    override fun getPostByQuery(query: String, exclude: List<PostDTO>): Single<PostDTO> {
        return Single
                .fromCallable { getPostItem(query, exclude) }
                .resumeErrors(query)
    }

    override fun getRandomPosts(count: Int, exclude: List<PostDTO>): Single<List<PostDTO>> {
        return Single
                .fromCallable { getPostItems(count, exclude = exclude) }
                .resumeErrors()
    }

    override fun getPostsByQuery(query: String, count: Int, exclude: List<PostDTO>): Single<List<PostDTO>> {
        return Single
                .fromCallable { getPostItems(count, query, exclude) }
                .resumeErrors(query)
    }

    private fun getPostItems(count: Int, query: String = "", exclude: List<PostDTO>): List<PostDTO> {
        var page = 0
        var posts = listOf<PostDTO>()
        do { posts += getFilteredPosts(++page, query, exclude) } while (posts.size < count)
        return posts.take(count)
    }

    private fun getPostItem(query: String = "", exclude: List<PostDTO>): PostDTO {
        var page = 0
        var filtered: List<PostDTO>
        do { filtered = getFilteredPosts(++page, query, exclude) } while (filtered.isEmpty())
        return filtered.random()
    }

    private fun getFilteredPosts(page: Int, query: String, exclude: List<PostDTO>): List<PostDTO> {
        return parsePage(page, query)
                .filter { !exclude.contains(it) }
                .filter { it.title.isNotBlank() && it.link.isNotBlank() }
    }

    @Throws(HttpStatusException::class)
    private fun parsePage(pageNumber: Int, query: String) = if (query.isBlank()) site.parse(pageNumber) else site.parse(pageNumber, query)

    private fun <T> Single<T>.resumeErrors(query: String = "") = this.onErrorResumeNext { t ->
        if (t is HttpStatusException && t.statusCode == 404) Single.error(NoPostsFound(query))
        else Single.error(t)
    }
}

