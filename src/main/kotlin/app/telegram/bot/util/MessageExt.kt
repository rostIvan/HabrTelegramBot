package app.telegram.bot.util

import app.telegram.bot.data.model.MessageText
import app.telegram.bot.data.model.Post
import app.telegram.bot.data.model.Weather
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.stream.Collector
import java.util.stream.Collectors

fun extractQuery(matcher: Matcher) = matcher.group(1).toString()
fun extractCount(matcher: Matcher) = matcher.group(1).toInt()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : MessageText> List<T>.toMessage() = when(T::class) {
    Weather::class -> formatWeatherForecast(this as List<Weather>)
    Post::class -> formatPosts(this as List<Post>)
    else -> this.collectWithNewLine { it.toMessage() }
}

fun formatCurrentDayWeather(weather: Weather): String = with(weather) {
    """
        Location: $location
        Time: $date
        Condition: $condition
        Temperature: $temperatureMin C°
        Link: $link
    """.trimIndent()
}

fun formatForecastDayWeather(weather: Weather): String = with(weather) {
    """
        Location: $location
        Date: $date
        Day: $day
        Condition: $condition
        Temperature(min): $temperatureMin C°
        Temperature(max): $temperatureMax C°
        Link: $link
    """.trimIndent()
}

fun formatWeatherForecast(list: List<Weather>): String {
    val location = list[0].location
    val link = list[0].link
    val forecast = list.collectWithNewLine { formatForecastItem(it) }
    return "Location: $location\n" +
            "Link: $link\n\n" +
            forecast
}

private fun formatForecastItem(weather: Weather): String {
    return with(weather) {
        """
                Date: $date
                Day: $day
                Condition: $condition
                Temperature(min): $temperatureMin C°
                Temperature(max): $temperatureMax C°
        """.trimIndent()
    }
}

fun formatPost(post: Post): String = with(post) { "> $link" }
fun formatPosts(list: List<Post>): String = list.collectWithNewLine { with(it) { "$title\n> $link" } }

fun newLineJoin(): Collector<CharSequence, *, String> = Collectors.joining("\n\n")
fun <T> List<T>.collectWithNewLine(mapTo: (T) -> String): String = this.stream().map { mapTo(it) }.collect(newLineJoin())