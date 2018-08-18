package app.telegram.bot.util

import app.telegram.bot.api.yahoo.WeatherJsonWrapper
import app.telegram.bot.data.Weather
import kotlin.math.roundToInt

object WeatherUtil {
    private const val DAY_OF_WEEK = 7

    fun toCurrentWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.CURRENT)
    fun toTodayWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.TODAY)
    fun toTomorrowWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.TOMORROW)

    fun toWeekWeather(weatherJsonWrapper: WeatherJsonWrapper) : List<Weather> {
        return IntRange(0, DAY_OF_WEEK - 1).map { index -> toWeatherObj(weatherJsonWrapper, Weather.Type.FORECAST, index) }.toList()
    }

    private fun toWeatherObj(weatherJsonWrapper: WeatherJsonWrapper, weatherType: Weather.Type, weekPosition : Int = -1) : Weather {
        val channel = weatherJsonWrapper.query.results.channel
        val location = channel.location
        return weatherByType(weatherType, channel, location, weekPosition)
    }

    private fun weatherByType(weatherType: Weather.Type, channel: WeatherJsonWrapper.Channel, location: WeatherJsonWrapper.Location, weekPosition: Int): Weather {
        return when (weatherType) {
            Weather.Type.CURRENT -> current(channel, location)
            Weather.Type.TODAY -> forecast(channel, location, 0)
            Weather.Type.TOMORROW -> forecast(channel, location, 1)
            Weather.Type.FORECAST -> forecast(channel, location, weekPosition)
        }
    }

    private fun current(channel: WeatherJsonWrapper.Channel, location: WeatherJsonWrapper.Location): Weather {
        val currentCondition = channel.item.currentCondition
        return Weather(
                location = location.formatLocation(),
                link = channel.link.formatLink(),
                date = currentCondition.date,
                condition = currentCondition.status,
                temperatureMin = fahrenheitToCelsius(currentCondition.temperature),
                temperatureMax = fahrenheitToCelsius(currentCondition.temperature))
    }

    private fun forecast(channel: WeatherJsonWrapper.Channel, location: WeatherJsonWrapper.Location, weekPosition: Int): Weather {
        val forecastDay = channel.item.forecast[weekPosition]
        return Weather(
                location = location.formatLocation(),
                link = channel.link.formatLink(),
                condition = forecastDay.status,
                date = forecastDay.date,
                day = forecastDay.day,
                temperatureMin = fahrenheitToCelsius(forecastDay.temperatureMin),
                temperatureMax = fahrenheitToCelsius(forecastDay.temperatureMax))
    }
}

private fun WeatherJsonWrapper.Location.formatLocation() =
        "${city.trim()}, ${country.trim()}(${region.trim()})"

// link example: http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2347539
private fun String.formatLink(): String = this
        .replaceBeforeLast("/*", "")
        .removePrefix("/*")
        .removeSuffix("/")

private fun fahrenheitToCelsius(fahrenheit: Int) = ((5.0 / 9.0) * (fahrenheit - 32.0)).roundToInt()
