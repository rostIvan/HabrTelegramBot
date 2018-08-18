package app.telegram.bot.util

import app.telegram.bot.data.Post
import app.telegram.bot.data.Weather
import java.util.stream.Collectors

fun Weather.toMessage(): String = this.let {
    if (day.isBlank() && temperatureMin == temperatureMax)
        formatCurrentDayWeather(it)
    else
        formatForecastDayWeather(it)
}

fun Post.toMessage(): String = with(this) { "$title >>> <a href=\"$link\">Link</a>" + if (description.isBlank()) "" else "\n$description" }

@Suppress("UNCHECKED_CAST")
inline fun <reified T> List<T>.toMessage(): String = when (T::class) {
    Weather::class -> formatWeekWeatherForecast(this as List<Weather>)
    Post::class -> formatPosts(this as List<Post>)
    else -> throw IllegalArgumentException("It isn't valid objects for transform to message (${T::class.java.simpleName})")
}

private fun formatCurrentDayWeather(weather: Weather): String = with(weather) {
    """
        Location: $location
        Time: $date
        Condition: $condition
        Temperature: $temperatureMin C°
        Link: $link
    """.trimIndent()
}

private fun formatForecastDayWeather(weather: Weather): String = with(weather) {
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

fun formatWeekWeatherForecast(list: List<Weather>): String {
    val location = list[0].location
    val link = list[0].link
    val forecast = list.stream().map {
        with(it) {
            """
                Date: $date
                Day: $day
                Condition: $condition
                Temperature(min): $temperatureMin C°
                Temperature(max): $temperatureMax C°
            """.trimIndent()
        }
    }.collect(newLineJoin())
    return "Location: $location\n" +
            "Link: $link\n\n" +
            forecast
}

fun formatPosts(list: List<Post>): String = list.stream().map { it.toMessage() }.collect(newLineJoin())

private fun newLineJoin() = Collectors.joining("\n\n")