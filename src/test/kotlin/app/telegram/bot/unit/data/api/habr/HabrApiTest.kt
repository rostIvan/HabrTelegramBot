package app.telegram.bot.unit.data.api.habr

import app.telegram.bot.business.error.NoPostsFound
import app.telegram.bot.data.api.habr.HabrApi
import app.telegram.bot.data.api.habr.HabrSiteParser
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.model.PostDTO
import org.jsoup.HttpStatusException
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class HabrApiTest {
    @Mock lateinit var siteParser: HabrSiteParser
    lateinit var postApi: PostApi

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        postApi = HabrApi(siteParser)
    }
    
    @Test fun getRandomPost_ifNoPostsFound_shouldThrowException() {
        val firstPage = listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        val secondPage = listOf(titleModel("Create applications anywhere"), titleModel("New cool things in Java 11 and Stream API"))
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        `when`(siteParser.parse(2)).thenReturn(secondPage)
        `when`(siteParser.parse(3)).thenThrow(HttpStatusException("", 404, ""))

        val exclude = listOf(firstPage[0], firstPage[1], secondPage[0], secondPage[1])
        postApi.getRandomPost(exclude)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts")

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1)
        inOrder.verify(siteParser).parse(2)
        inOrder.verify(siteParser).parse(3)
    }

    @Test fun getRandomPost_ifPostFound_onFirstPage_shouldReturnIt() {
        val firstPage = listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        val exclude = listOf(firstPage[0])
        postApi.getRandomPost(exclude)
                .test()
                .assertNoErrors()
                .assertResult(firstPage[1])
    }

    @Test fun getRandomPost_ifPostFound_onNextPage_shouldReturnIt() {
        val firstPage = listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        val secondPage = listOf(titleModel("Create applications anywhere"), titleModel("New cool things in Java 11 and Stream API"))
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        `when`(siteParser.parse(2)).thenReturn(secondPage)
        postApi.getRandomPost(exclude = firstPage)
                .test()
                .assertNoErrors()
                .assertValue { secondPage.contains(it) }

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1)
        inOrder.verify(siteParser).parse(2)
    }

    @Test fun getRandomPostByQuery_ifNoPostsFound_shouldThrowException() {
        val query = "Vasya"
        val firstPage = listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)
        `when`(siteParser.parse(2, query)).thenThrow(HttpStatusException("", 404, ""))
        postApi.getPostByQuery(query, exclude = firstPage)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts by query['$query']")
    }

    @Test fun getRandomPostByQuery_ifPostFound_onFirstPage_shouldReturnIt() {
        val query = "R lang"
        val firstPage = listOf(titleModel("R lang in your projects"), titleModel("R lang and how it use for statistic"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)
        val exclude = listOf(firstPage[1])
        postApi.getPostByQuery(query, exclude)
                .test()
                .assertNoErrors()
                .assertResult(firstPage[0])
                .assertValue { it.title.contains(query, ignoreCase = true) }
    }

    @Test fun getRandomPostByQuery_ifPostFound_onNextPage_shouldReturnIt() {
        val query = "RxJava"
        val firstPage = listOf(titleModel("RxJava vs RxKotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        val secondPage = listOf(titleModel("RxJava backend side"), titleModel("RxJava for service layer"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)
        `when`(siteParser.parse(2, query)).thenReturn(secondPage)
        postApi.getPostByQuery(query, exclude = firstPage)
                .test()
                .assertNoErrors()
                .assertValue { secondPage.contains(it) && it.title.contains(query, ignoreCase = true)}

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1, query)
        inOrder.verify(siteParser).parse(2, query)
    }

    @Test fun getRandomPosts_ifNoPostsFound_shouldThrowException() {
        val firstPage = emptyList<PostDTO>()
        val secondPage = listOf(titleModel("Hello world"), titleModel("SOLID"))
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        `when`(siteParser.parse(2)).thenReturn(secondPage)
        `when`(siteParser.parse(3)).thenThrow(HttpStatusException("", 404, ""))
        postApi.getRandomPosts(3, exclude = secondPage)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts")

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1)
        inOrder.verify(siteParser).parse(2)
    }

    @Test fun getRandomPostsEmptyExclude_ifPostsFound_onFirstPage_shouldReturnThem() {
        val firstPage = listOf(
                titleModel("Java vs Kotlin in action"),
                titleModel("Forget RxJava, use kotlin coroutines"),
                titleModel("What time do you need to write a testable code?")
        )
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        postApi.getRandomPosts(count = 2, exclude = emptyList())
                .test()
                .assertNoErrors()
                .assertResult(listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines")))
        verify(siteParser, times(1)).parse(1)
        verify(siteParser, never()).parse(2)
    }

    @Test fun getRandomPosts_ifPostsFound_onFirstPage_shouldReturnThem() {
        val firstPage = listOf(
                titleModel("Java vs Kotlin in action"),
                titleModel("How to map objects in kotlin. True way"),
                titleModel("Forget RxJava, use kotlin coroutines"),
                titleModel("What time do you need to write a testable code?")
        )
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        val exclude = listOf(titleModel("How to map objects in kotlin. True way"))
        postApi.getRandomPosts(count = 3, exclude = exclude)
                .test()
                .assertNoErrors()
                .assertValue { it.size == 3 }
                .assertValue { it.all { !exclude.contains(it) } }
                .assertResult(listOf(
                        titleModel("Java vs Kotlin in action"),
                        titleModel("Forget RxJava, use kotlin coroutines"),
                        titleModel("What time do you need to write a testable code?")
                ))
        verify(siteParser, times(1)).parse(1)
        verify(siteParser, never()).parse(2)
    }

    @Test fun getRandomPosts_ifPostsFound_onNextPages_shouldReturnThem() {
        val firstPage = listOf(
                titleModel("Java vs Kotlin in action"),
                titleModel("Forget RxJava, use kotlin coroutines"),
                titleModel("What time do you need to write a testable code?")
        )
        val secondPage = listOf(
                titleModel("Clean architecture. Uncle Bob"),
                titleModel("How to map objects in kotlin. True way")
        )
        val thirdPage = listOf(
                titleModel("Why you should use reactive style programming?"),
                titleModel("Hello world on 25 different lang")
        )
        `when`(siteParser.parse(1)).thenReturn(firstPage)
        `when`(siteParser.parse(2)).thenReturn(secondPage)
        `when`(siteParser.parse(3)).thenReturn(thirdPage)
        val exclude = listOf(
                titleModel("Java vs Kotlin in action"),
                titleModel("Forget RxJava, use kotlin coroutines"),
                titleModel("How to map objects in kotlin. True way")
        )
        postApi.getRandomPosts(count = 3, exclude = exclude)
                .test()
                .assertNoErrors()
                .assertValue { it.size == 3 }
                .assertValue { it.all { !exclude.contains(it) } }
                .assertResult(listOf(
                        titleModel("What time do you need to write a testable code?"),
                        titleModel("Clean architecture. Uncle Bob"),
                        titleModel("Why you should use reactive style programming?")
                ))

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1)
        inOrder.verify(siteParser).parse(2)
        inOrder.verify(siteParser).parse(3)
    }

    @Test fun getRandomPostsByQuery_ifNoPostsFound_shouldThrowException() {
        val query = "Petya"
        val firstPage = listOf(titleModel("Java vs Kotlin in action"), titleModel("Forget RxJava, use kotlin coroutines"))
        val secondPage = listOf(titleModel("Create applications anywhere"), titleModel("New cool things in Java 11 and Stream API"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)
        `when`(siteParser.parse(2, query)).thenReturn(secondPage)
        `when`(siteParser.parse(3, query)).thenThrow(HttpStatusException("", 404, ""))

        val exclude = firstPage + secondPage
        postApi.getPostsByQuery(query, exclude = exclude)
                .test()
                .assertError(NoPostsFound::class.java)
                .assertErrorMessage("Can't find posts by query['$query']")

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser).parse(1, query)
        inOrder.verify(siteParser).parse(2, query)
        inOrder.verify(siteParser).parse(3, query)
    }

    @Test fun getRandomPostsByQueryEmptyExclude_ifPostsFound_onFirstPage_shouldReturnThem() {
        val query = "Kotlin extensions"
        val firstPage = listOf(titleModel("Java util classes vs extensions"), titleModel("Kotlin extensions samples"), titleModel("Kotlin extensions, inline function and another cool things in lang"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)

        postApi.getPostsByQuery(query, count = 2, exclude = emptyList())
                .test()
                .assertNoErrors()
                .assertValue { it.size == 2 }
                .assertResult(listOf(titleModel("Java util classes vs extensions"), titleModel("Kotlin extensions samples")))

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser, times(1)).parse(1, query)
        inOrder.verify(siteParser, never()).parse(2, query)
    }

    @Test fun getRandomPostsByQuery_ifPostsFound_onNextPages_shouldReturnThem() {
        val query = "RoR"
        val firstPage = listOf(titleModel("How to use RoR for authentication"), titleModel("Ruby RoR get started"))
        val secondPage = listOf(titleModel("Ruby RoR framework"), titleModel("Ruby developers about using RoR in production"))
        val thirdPage = listOf(titleModel("RoR. Some words about ORM in framework"), titleModel("RoR like a good solution your problems"))
        `when`(siteParser.parse(1, query)).thenReturn(firstPage)
        `when`(siteParser.parse(2, query)).thenReturn(secondPage)
        `when`(siteParser.parse(3, query)).thenReturn(thirdPage)

        postApi.getPostsByQuery(query, count = 5, exclude = listOf(titleModel("Ruby RoR framework")))
                .test()
                .assertNoErrors()
                .assertValue { it.size == 5 }
                .assertResult(listOf(
                        titleModel("How to use RoR for authentication"),
                        titleModel("Ruby RoR get started"),
                        titleModel("Ruby developers about using RoR in production"),
                        titleModel("RoR. Some words about ORM in framework"),
                        titleModel("RoR like a good solution your problems")
                ))

        val inOrder = inOrder(siteParser)
        inOrder.verify(siteParser, times(1)).parse(1, query)
        inOrder.verify(siteParser, times(1)).parse(2, query)
        inOrder.verify(siteParser, times(1)).parse(3, query)
    }

    private fun titleModel(title: String) = PostDTO(title, "https://habr.com/post/420969/", "", listOf(), "", "")
}