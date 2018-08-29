package app.telegram.bot.data.api.yahoo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherJsonWrapper(@SerializedName("query") @Expose val query: Query) {
    data class Query(@SerializedName("results") @Expose val results: Results)
    data class Results(@SerializedName("channel") @Expose val channel: Channel)
    data class Channel(@SerializedName("link") @Expose val link: String,
                       @SerializedName("location") @Expose val location: Location,
                       @SerializedName("item") @Expose val item: Item)

    data class Location(@SerializedName("city") @Expose val city: String,
                        @SerializedName("country") @Expose val country: String,
                        @SerializedName("region") @Expose val region: String)
    data class Item(@SerializedName("condition") @Expose val currentCondition: Condition,
                    @SerializedName("forecast") @Expose val forecast: List<Forecast>)

    data class Condition(@SerializedName("date") @Expose val date: String,
                         @SerializedName("temp") @Expose val temperature: Int,
                         @SerializedName("text") @Expose val status: String)
    data class Forecast(@SerializedName("date") @Expose val date: String,
                        @SerializedName("day") @Expose val day: String,
                        @SerializedName("low") @Expose val temperatureMin: Int,
                        @SerializedName("high") @Expose val temperatureMax: Int,
                        @SerializedName("text") @Expose val status: String)
}