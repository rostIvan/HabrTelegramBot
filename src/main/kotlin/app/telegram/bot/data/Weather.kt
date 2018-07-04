package app.telegram.bot.data

data class Weather(val location: String,
                   val link: String,
                   val date: String,
                   val day: String = "",
                   var condition: String,
                   var temperatureMin: Int,
                   var temperatureMax: Int) {

    enum class Type { CURRENT, TODAY, TOMORROW, FORECAST }
}