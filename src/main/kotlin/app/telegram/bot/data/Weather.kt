package app.telegram.bot.data

import com.google.gson.annotations.Expose

data class Weather(@Expose val location: String,
                   @Expose val link: String,
                   @Expose val date: String,
                   @Expose var condition: String,
                   @Expose var temperatureMin: Int,
                   @Expose var temperatureMax: Int)