package app.telegram.bot.util

import app.telegram.bot.data.model.MessageText
import app.telegram.bot.data.model.Post
import java.util.regex.Matcher
import java.util.stream.Collector
import java.util.stream.Collectors

fun extractQuery(matcher: Matcher) = matcher.group(1).toString()
fun extractCount(matcher: Matcher) = matcher.group(1).toInt()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : MessageText> List<T>.toMessage() = when(T::class) {
    Post::class -> formatPosts(this as List<Post>)
    else -> this.collectWithNewLine { it.toMessage() }
}

fun formatPost(post: Post): String = with(post) { "> $link" }
fun formatPosts(list: List<Post>): String = list.collectWithNewLine { with(it) { "$title\n> $link" } }

fun newLineJoin(): Collector<CharSequence, *, String> = Collectors.joining("\n\n")
fun <T> List<T>.collectWithNewLine(mapTo: (T) -> String): String = this.stream().map { mapTo(it) }.collect(newLineJoin())