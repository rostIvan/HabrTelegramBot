package app.telegram.bot.business.inheritence

import app.telegram.bot.data.Weather
import io.reactivex.Single

interface WeatherProvider {
    fun getCurrentWeather() : Single<Weather>
    fun getTodayWeather() : Single<Weather>
    fun getTomorrowWeather() : Single<Weather>
    fun getWeekWeather() : Single<List<Weather>>
}