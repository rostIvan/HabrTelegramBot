package app.telegram.bot.unit.api.habr

import app.telegram.bot.api.habr.HabrApi
import app.telegram.bot.api.habr.PostApi
import app.telegram.bot.api.habr.PostParseWrapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HabrApiTest {

    lateinit var postApi: PostApi

    @Before fun before() {
        postApi = HabrApi()
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

    @Test fun getRandomPosts_shouldReturnTenValidPosts() {
        val posts = postApi.getRandomPosts().blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(10)
        posts.forEach { checkPost(it) }
    }

    @Test fun getByQueryPosts_shouldReturnTenValidPosts() {
        val query = "Hello world"
        val posts = postApi.getPostsByQuery(query).blockingGet()
        assertThat(posts).isNotNull
        assertThat(posts).isNotEmpty
        assertThat(posts).hasSize(10)
        posts.forEach {
            checkPost(it)
            val containsQuery = containsQuery(it, query)
            assertThat(containsQuery).isEqualTo(true)
        }
    }

    @Test fun getRandomPosts_ifCountTooBig_shouldThrowException() {
        val expectMessage = "Sorry, but count[20] must be in a range"
        expectExceptionWithMessage(expectMessage) {
            postApi.getRandomPosts(20).blockingGet()
        }
    }

    @Test fun getRandomPosts_ifCountTooSmall_shouldThrowException() {
        val expectMessage = "Sorry, but count[-1] must be in a range"
        expectExceptionWithMessage(expectMessage) {
            postApi.getRandomPosts(-1).blockingGet()
        }
    }


    @Test fun getPostsByQuery_ifCountTooSmall_shouldThrowException() {
        val expectMessage = "Sorry, but count[0] must be in a range"
        expectExceptionWithMessage(expectMessage) {
            postApi.getRandomPosts(0).blockingGet()
        }
    }

    @Test fun getPostsByQuery_ifCountTooBig_shouldThrowException() {
        val expectMessage = "Sorry, but count[14] must be in a range"
        expectExceptionWithMessage(expectMessage) {
            postApi.getRandomPosts(14).blockingGet()
        }
    }

    private fun containsQuery(post: PostParseWrapper, query: String): Boolean {
        val titleContainsQuery = post.title.containsIn(query)
        val linkContainsQuery = post.link.containsIn(query)
        val descriptionContainsQuery = post.description.containsIn(query)
        return titleContainsQuery || linkContainsQuery || descriptionContainsQuery
    }

    private fun checkPost(post: PostParseWrapper) {
        assertThat(post).isNotNull
        assertThat(post.title).isNotBlank()
        assertThat(post.description).isNotBlank()
        assertThat(post.postByUser).isNotBlank()
        assertThat(post.date).isNotBlank()
        assertThat(post.link).isNotBlank().contains("https://habr.com")
        assertThat(post.tags).isNotEmpty
    }

    private inline fun expectExceptionWithMessage(message: String, func: () -> Unit) {
        try {
            func.invoke()
            fail("Expected but not invoke")
        } catch (ex: Exception) {
            assertThat(ex).hasMessageContaining(message)
        }
    }

    private fun String.containsIn(query: String) = this.toLowerCase()
            .replace("-", " ")
            .replace(",", "")
            .contains(query.toLowerCase())
}