package app.telegram.bot.business.implementation

import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.api.yahoo.WeatherApi.Companion.defaultLocation
import app.telegram.bot.business.inheritence.WeatherProvider
import app.telegram.bot.data.Weather
import app.telegram.bot.util.WeatherUtil
import io.reactivex.Single
import org.springframework.beans.factory.annotation.Autowired

class WeatherProviderImpl(@Autowired private val weatherApi: WeatherApi) : WeatherProvider {
    override fun getCurrentWeather(): Single<Weather> = weatherApi.get(defaultLocation).map(WeatherUtil::toCurrentWeather)
    override fun getTodayWeather(): Single<Weather> = weatherApi.get(defaultLocation).map(WeatherUtil::toTodayWeather)
    override fun getTomorrowWeather(): Single<Weather> = weatherApi.get(defaultLocation).map(WeatherUtil::toTomorrowWeather)
    override fun getWeekWeather(): Single<List<Weather>> = weatherApi.get(defaultLocation).map(WeatherUtil::toWeekWeather)
}