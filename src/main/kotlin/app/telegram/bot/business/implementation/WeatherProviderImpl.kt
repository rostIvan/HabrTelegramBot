package app.telegram.bot.business.implementation

import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.api.yahoo.WeatherApi.Companion.defaultLocation
import app.telegram.bot.api.yahoo.WeatherJsonWrapper
import app.telegram.bot.business.inheritence.WeatherProvider
import app.telegram.bot.data.Weather
import app.telegram.bot.util.WeatherUtil
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired

class WeatherProviderImpl(@Autowired private val weatherApi: WeatherApi) : WeatherProvider {
    override fun getCurrentWeather(): Single<Weather> = apiCall()
            .map(WeatherUtil::toCurrentWeather)

    override fun getTodayWeather(): Single<Weather> = apiCall()
            .map(WeatherUtil::toTodayWeather)

    override fun getTomorrowWeather(): Single<Weather> = apiCall()
            .map(WeatherUtil::toTomorrowWeather)

    override fun getWeekWeather(): Single<List<Weather>> = apiCall()
            .map(WeatherUtil::toWeekWeather)

    private fun apiCall() : Single<WeatherJsonWrapper> = weatherApi.get(defaultLocation)
}