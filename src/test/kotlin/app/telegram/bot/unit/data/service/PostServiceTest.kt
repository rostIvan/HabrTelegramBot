package app.telegram.bot.unit.data.service

import app.telegram.bot.anyObj
import app.telegram.bot.business.error.NoPostsFound
import app.telegram.bot.business.error.PostRangeError
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.data.service.post.PostServiceImpl
import app.telegram.bot.data.storage.dao.DaoStorage
import app.telegram.bot.data.storage.hibernate.PostDbModel
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class PostServiceTest {
    @Mock lateinit var api: PostApi
    @Mock lateinit var storage: DaoStorage<PostDbModel>
    lateinit var postService: PostService

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        postService = PostServiceImpl(api, storage)
    }

    @Test fun getRandomPost_ifPostFound_shouldReturnIt() {
        `when`(api.getRandomPost(anyList())).thenReturn(Single.just(simplePostModel("Create java hello world", "https://habr.com/post/421229/")))
        postService.getRandomPost()
                .test()
                .assertNoErrors()
                .assertValue { post ->  post.title == "Create java hello world" && post.link == "https://habr.com/post/421229/" }
        verify(storage, times(1)).save(anyObj())
    }

    @Test fun getRandomPostByQuery_ifPostFound_shouldReturnIt() {
        val query = "java"
        `when`(api.getPostByQuery(query)).thenReturn(Single.just(simplePostModel("Create $query hello world", "https://habr.com/post/421229/")))
        postService.getPostByQuery(query)
                .test()
                .assertNoErrors()
                .assertValue { post -> post.title.contains(query, ignoreCase = true) }
                .assertValue { post ->  post.title == "Create java hello world" && post.link == "https://habr.com/post/421229/" }
        verify(storage, times(1)).save(anyObj())
    }

    @Test fun getRandomPosts_ifPostsFound_shouldReturnThem() {
        val count = 3
        val mockApi = listOf(
                simplePostModel("Create java hello world", "https://habr.com/post/421229/"),
                simplePostModel("Create ruby hello world", "https://habr.com/post/125135/"),
                simplePostModel("Create python hello world", "https://habr.com/post/255678/")
        )
        `when`(api.getRandomPosts(count)).thenReturn(Single.just(mockApi))
        postService.getRandomPosts(count)
                .test()
                .assertNoErrors()
                .assertValue { it.size == 3 }
                .assertValue {
                    it[0].title == mockApi[0].title && it[0].link == mockApi[0].link &&
                    it[1].title == mockApi[1].title && it[1].link == mockApi[1].link &&
                    it[2].title == mockApi[2].title && it[2].link == mockApi[2].link
                }
        verify(storage, times(1)).saveAll(anyObj())
    }

    @Test fun getRandomPostsByQuery_ifPostsFound_shouldReturnThem() {
        val count = 3
        val query = "hello world"
        val mockApi = listOf(
                simplePostModel("Create java $query", "https://habr.com/post/421229/"),
                simplePostModel("Create ruby $query", "https://habr.com/post/125135/"),
                simplePostModel("Create python $query", "https://habr.com/post/255678/")
        )
        `when`(api.getPostsByQuery(query, count)).thenReturn(Single.just(mockApi))
        postService.getPostsByQuery(query, count)
                .test()
                .assertNoErrors()
                .assertValue { it.size == 3 }
                .assertValue { it.all { it.title.contains(query) } }
                .assertValue {
                    it[0].title == mockApi[0].title && it[0].link == mockApi[0].link &&
                    it[1].title == mockApi[1].title && it[1].link == mockApi[1].link &&
                    it[2].title == mockApi[2].title && it[2].link == mockApi[2].link
                }
        verify(storage, times(1)).saveAll(anyObj())
    }

    @Test fun getRandomPost_ifNoPostFound_shouldReturnError() {
        `when`(api.getRandomPost(anyList())).thenReturn(Single.error(NoPostsFound()))
        postService.getRandomPost()
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts")
    }


    @Test fun getRandomPosts_ifNoPostsFound_shouldReturnError() {
        `when`(api.getRandomPosts(anyInt(), anyList())).thenReturn(Single.error(NoPostsFound()))
        postService.getRandomPosts(4)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts")
    }

    @Test fun getPostByQuery_ifNoPostFound_shouldReturnError() {
        val query = "**Abc"
        `when`(api.getPostByQuery(query)).thenReturn(Single.error(NoPostsFound(query)))
        postService.getPostByQuery(query)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts by query['$query']")
    }

    @Test fun getPostsByQuery_ifNoPostsFound_shouldReturnError() {
        val query = "___=="
        val count = 2
        `when`(api.getPostsByQuery(query, count)).thenReturn(Single.error(NoPostsFound(query)))
        postService.getPostsByQuery(query, count)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts by query['$query']")
    }

    @Test fun getPostsByQuery_ifOutOfRangeCount_shouldReturnError() {
        val query = "Hello"
        val count = 100
        val mockApi = listOf(
                simplePostModel("Create java $query", "https://habr.com/post/421229/"),
                simplePostModel("Create ruby $query", "https://habr.com/post/125135/"),
                simplePostModel("Create python $query", "https://habr.com/post/255678/")
        )
        `when`(api.getPostsByQuery(query, count)).thenReturn(Single.just(mockApi))
        postService.getPostsByQuery(query, count)
                .test()
                .assertError(PostRangeError::class.java)
                .assertErrorMessage("Number of posts should be in range[1, 80] but you choose $count")
    }

    @Test fun getRandomPosts_ifOutOfRangeCount_shouldReturnError() {
        val query = "Hello"
        val count = 0
        val mockApi = listOf(
                simplePostModel("Create java $query", "https://habr.com/post/421229/"),
                simplePostModel("Create ruby $query", "https://habr.com/post/125135/"),
                simplePostModel("Create python $query", "https://habr.com/post/255678/")
        )
        `when`(api.getPostsByQuery(query, count)).thenReturn(Single.just(mockApi))
        postService.getPostsByQuery(query, count)
                .test()
                .assertError(PostRangeError::class.java)
                .assertErrorMessage("Number of posts should be in range[1, 80] but you choose $count")
    }

    private fun simplePostModel(title: String, link: String) = PostDTO(title, link, "", listOf(), "", "")
}