package app.telegram.bot.util

import app.telegram.bot.api.yahoo.WeatherJsonWrapper
import app.telegram.bot.data.Weather

object WeatherUtil {
    fun toCurrentWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.CURRENT)
    fun toTodayWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.TODAY)
    fun toTomorrowWeather(weatherJsonWrapper: WeatherJsonWrapper) : Weather = toWeatherObj(weatherJsonWrapper, Weather.Type.TOMORROW)

    fun toWeekWeather(weatherJsonWrapper: WeatherJsonWrapper) : List<Weather> {
        val forecast = weatherJsonWrapper.query.results.channel.item.forecast
        val weekWeatherForecast = mutableListOf<Weather>()
        for (index in forecast.indices) {
            if (index == 7) break
            weekWeatherForecast.add(toWeatherObj(weatherJsonWrapper, Weather.Type.FORECAST, index))
        }
        return weekWeatherForecast
    }

    private fun toWeatherObj(weatherJsonWrapper: WeatherJsonWrapper, weatherType: Weather.Type, weekPosition : Int = -1) : Weather {
        val channel = weatherJsonWrapper.query.results.channel
        val location = channel.location
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
                location = "${location.city.trim()}, ${location.country.trim()}(${location.region.trim()})",
                link = channel.link,
                date = currentCondition.date,
                condition = currentCondition.status,
                temperatureMin = currentCondition.temperature, temperatureMax = currentCondition.temperature)
    }

    private fun forecast(channel: WeatherJsonWrapper.Channel, location: WeatherJsonWrapper.Location, weekPosition: Int): Weather {
        val forecastDay = channel.item.forecast[weekPosition]
        return Weather(
                location = "${location.city.trim()}, ${location.country.trim()}(${location.region.trim()})",
                link = channel.link,
                condition = forecastDay.status,
                date = forecastDay.date, day = forecastDay.day,
                temperatureMin = forecastDay.temperatureMin, temperatureMax = forecastDay.temperatureMax)
    }
}
