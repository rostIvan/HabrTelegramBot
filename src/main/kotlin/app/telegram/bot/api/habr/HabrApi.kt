package app.telegram.bot.api.habr

import app.telegram.bot.util.random
import io.reactivex.Single
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class HabrApi : PostApi {

    private val baseUrl = "https://habr.com"

    override fun getRandomPost(): Single<PostParseWrapper> {
        val document = Jsoup.connect(baseUrl).get()
        val item = getRandomItem(document)
        val post = postModel(item)
        return Single.just(post)
    }

    override fun getPostByQuery(query: String): Single<PostParseWrapper> {
        val document = Jsoup.connect("$baseUrl/search/?q=$query").get()
        val item = getRandomItem(document)
        val post = postModel(item)
        return Single.just(post)
    }

    override fun getRandomPosts(count: Int): Single<List<PostParseWrapper>> {
        val document = Jsoup.connect(baseUrl).get()
        val items = getItems(document)
        val posts = getPartOfPosts(items, count)
        return Single.just(posts)
    }

    override fun getPostsByQuery(query: String, count: Int): Single<List<PostParseWrapper>> {
        val document = Jsoup.connect("$baseUrl/search/?q=$query").get()
        val items = getItems(document)
        val posts = getPartOfPosts(items, count)
        return Single.just(posts)
    }

    private fun getPartOfPosts(items: Elements, count: Int): List<PostParseWrapper> {
        if (count !in (1..items.size)) throw Exception("Sorry, but count[$count] must be in a range[1, ${items.size}]")
        val posts = arrayListOf<PostParseWrapper>()
        items.forEachIndexed { index, element ->
            if (index > count - 1) return@forEachIndexed
            posts.add(postModel(element))
        }
        return posts
    }

    private fun getRandomItem(document: Document): Element {
        val items = getItems(document) // all <li> elements start with `post_` is an our posts on html page
        val random = (0 until items.size).random()
        return items[random]
    }

    private fun getItems(document: Document) = document.select("li[id^=post_]")

    private fun getTitle(element: Element) = element.getElementsByClass("post__title_link").text()
    private fun getNick(element: Element) = element.getElementsByClass("user-info__nickname_small").text()
    private fun getDescription(element: Element) = element.getElementsByClass("post__text").text()
    private fun getLink(element: Element) = element.getElementsByClass("post__title_link").attr("href")
    private fun getDate(element: Element) = element.getElementsByClass("post__time").text()
    private fun getTags(element: Element) = element.getElementsByClass("inline-list__item_hub").text().split(",")

    private fun postModel(item: Element): PostParseWrapper {
        return PostParseWrapper(
                title = getTitle(item),
                link = getLink(item),
                description = getDescription(item),
                postByUser = getNick(item),
                tags = getTags(item),
                date = getDate(item)
        )
    }
}