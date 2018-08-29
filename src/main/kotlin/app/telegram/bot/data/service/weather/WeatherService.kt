package app.telegram.bot.data.service.weather

import app.telegram.bot.data.model.Weather
import io.reactivex.Single

interface WeatherService {
    fun getCurrentWeather() : Single<Weather>
    fun getTodayWeather() : Single<Weather>
    fun getTomorrowWeather() : Single<Weather>
    fun getWeekWeather() : Single<List<Weather>>
}