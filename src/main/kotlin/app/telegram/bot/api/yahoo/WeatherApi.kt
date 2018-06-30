package app.telegram.bot.api.yahoo

import io.reactivex.Single
import retrofit2.http.GET

interface WeatherApi {
    companion object {
        //"select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"ivano-frankivsk, ua\")"
        const val query: String = "select%20*%20from%20weather.forecast%20" +
                "where%20woeid%20in%20(" +
                    "select%20woeid%20from%20geo.places(1)%20where%20text%3D%22ivano-frankivsk%2C%20ua%22" +
                ")" +
                "&format=json" +
                "&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
    }

    @GET("v1/public/yql?q=$query")
    fun get() : Single<WeatherJsonWrapper>
}