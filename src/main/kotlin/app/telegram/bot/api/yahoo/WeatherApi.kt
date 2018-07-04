package app.telegram.bot.api.yahoo

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    companion object {
        val defaultLocation = locationQuery("ivano-frankivsk", "ua")
        fun locationQuery(city: String, region: String) = "select * from weather.forecast where woeid in (select woeid from geo.places(1) " +
                "where text=\"$city, $region\")"
    }

    @GET("v1/public/yql?format=json")
    fun get(@Query("q") query: String) : Single<WeatherJsonWrapper>
}