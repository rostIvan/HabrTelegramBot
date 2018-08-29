package app.telegram.bot.data.model

import app.telegram.bot.util.formatCurrentDayWeather
import app.telegram.bot.util.formatForecastDayWeather

data class Weather(val location: String,
                   val link: String,
                   val date: String,
                   val day: String = "",
                   var condition: String,
                   var temperatureMin: Int,
                   var temperatureMax: Int) : MessageText {

    enum class Type { CURRENT, TODAY, TOMORROW, FORECAST }

    override fun toMessage(): String = this.let {
        if (day.isBlank() && temperatureMin == temperatureMax) formatCurrentDayWeather(it)
        else formatForecastDayWeather(it)
    }
}