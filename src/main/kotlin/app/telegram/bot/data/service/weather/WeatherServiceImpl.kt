package app.telegram.bot.data.service.weather

import app.telegram.bot.data.api.yahoo.WeatherApi
import app.telegram.bot.data.api.yahoo.WeatherApi.Companion.defaultLocation
import app.telegram.bot.data.api.yahoo.WeatherJsonWrapper
import app.telegram.bot.data.model.Weather
import app.telegram.bot.util.WeatherUtil.toCurrentWeather
import app.telegram.bot.util.WeatherUtil.toTodayWeather
import app.telegram.bot.util.WeatherUtil.toTomorrowWeather
import app.telegram.bot.util.WeatherUtil.toWeekWeather
import io.reactivex.Single
import org.springframework.stereotype.Service

@Service
class WeatherServiceImpl(private val weatherApi: WeatherApi) : WeatherService {
    override fun getCurrentWeather(): Single<Weather> = apiCall()
            .map(::toCurrentWeather)

    override fun getTodayWeather(): Single<Weather> = apiCall()
            .map(::toTodayWeather)

    override fun getTomorrowWeather(): Single<Weather> = apiCall()
            .map(::toTomorrowWeather)

    override fun getWeekWeather(): Single<List<Weather>> = apiCall()
            .map(::toWeekWeather)

    private fun apiCall() : Single<WeatherJsonWrapper> = weatherApi.get(defaultLocation)
}