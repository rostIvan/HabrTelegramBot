package app.telegram.bot.data.api.habr

import app.telegram.bot.data.model.PostDTO
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

private const val baseUrl = "https://habr.com"

open class HabrSiteParser {

    @Throws(HttpStatusException::class)
    open fun parse(pageNumber: Int) = getPage(pageNumber).items().toPostModels()
    @Throws(HttpStatusException::class)
    open fun parse(pageNumber: Int, query: String) = getPageWithQuery(pageNumber, query).items().toPostModels()

    private fun getPage(number: Int) = Jsoup.connect("$baseUrl/page$number/").get()
    private fun getPageWithQuery(number: Int, query: String) = Jsoup.connect("$baseUrl/search/page$number/?q=$query").get()

    private fun getItems(document: Document) = document.select("li[id^=post_]")

    private fun getTitle(element: Element): String = element.getElementsByClass("post__title_link").text()
    private fun getNick(element: Element) = element.getElementsByClass("user-info__nickname_small").text()
    private fun getDescription(element: Element) = element.getElementsByClass("post__text").text()
    private fun getLink(element: Element) = element.getElementsByClass("post__title_link").attr("href")
    private fun getDate(element: Element) = element.getElementsByClass("post__time").text()
    private fun getTags(element: Element) = element.getElementsByClass("inline-list__item_hub").text().split(",")

    private fun postModel(item: Element): PostDTO {
        return PostDTO(
                title = getTitle(item),
                link = getLink(item),
                description = getDescription(item),
                postByUser = getNick(item),
                tags = getTags(item),
                date = getDate(item)
        )
    }
    private fun Elements.toPostModels() = this.map { postModel(it) }
    private fun Document.items() = getItems(this)
}