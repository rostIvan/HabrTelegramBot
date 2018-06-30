package app.telegram.bot.business.inheritence

import app.telegram.bot.data.Weather
import io.reactivex.Observable

interface WeatherProvider {
    fun getCurrentWeather() : Observable<Weather>
    fun getTodayWeather() : Observable<Weather>
    fun getTomorrowWeather() : Observable<Weather>
    fun getWeekWeather() : Observable<List<Weather>>
}