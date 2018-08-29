package app.telegram.bot.integration

import app.telegram.bot.data.api.habr.HabrApi
import app.telegram.bot.data.api.habr.HabrSiteParser
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.PostDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class PostParseIntegrationTest {

    lateinit var postApi: PostApi

    @Before fun before() {
        postApi = HabrApi(HabrSiteParser())
    }

    @Test fun getRandomPost_shouldReturnValidPost() {
        val post = postApi.getRandomPost().blockingGet()
        checkPost(post)
    }

    @Test fun getPostByRustQuery_shouldReturnRustPost() {
        val query = "rust"
        val post = postApi.getPostByQuery(query).blockingGet()
        checkPost(post)
        val containsQuery = containsQuery(post, query)
        assertThat(containsQuery).isEqualTo(true)
    }

    @Test fun getPostByRubyQuery_shouldReturnRubyPost() {
        val query = "ruby"
        val post = postApi.getPostByQuery(query).blockingGet()
        checkPost(post)
        val containsQuery = containsQuery(post, query)
        assertThat(containsQuery).isEqualTo(true)
    }

    @Test fun getRandomPosts_shouldReturnFiveValidPosts() {
        val posts = postApi.getRandomPosts(5).blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(5)
        posts.forEach { checkPost(it) }
    }

    @Test fun getRandomPosts_shouldReturnOneValidPost() {
        val query = "Java"
        val posts = postApi.getPostsByQuery(query, 1).blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(1)
        val post = posts.first()
        val containsQuery = containsQuery(post, query)
        checkPost(post)
        assertThat(containsQuery).isEqualTo(true)
    }

    @Test fun getRandomPostsWithoutArgs_shouldReturnFiveValidPosts() {
        val posts = postApi.getRandomPosts().blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(5)
        posts.forEach { checkPost(it) }
    }

    @Test fun getByQueryPosts_shouldReturnFiveValidPosts() {
        val query = "Hello world"
        val posts = postApi.getPostsByQuery(query).blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(5)
        posts.forEach {
            checkPost(it)
            val containsQuery = containsQuery(it, query)
            assertThat(containsQuery).isEqualTo(true)
        }
    }

    private fun checkPost(post: PostDTO) {
        assertThat(post).isNotNull
        assertThat(post.title).isNotBlank()
        assertThat(post.description).isNotBlank()
        assertThat(post.postByUser).isNotBlank()
        assertThat(post.date).isNotBlank()
        assertThat(post.link).isNotBlank().contains("https://habr.com")
        assertThat(post.tags).isNotEmpty
    }

    private fun containsQuery(post: PostDTO, query: String): Boolean {
        val titleContainsQuery = post.title.containsIn(query)
        val linkContainsQuery = post.link.containsIn(query)
        val descriptionContainsQuery = post.description.containsIn(query)
        return titleContainsQuery || linkContainsQuery || descriptionContainsQuery
    }

    private fun String.containsIn(query: String) = this.toLowerCase()
            .replace("-", " ")
            .replace(",", "")
            .contains(query.toLowerCase())
}