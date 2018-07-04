package app.telegram.bot.business.inheritence

import app.telegram.bot.data.Weather

interface ChatManager {
    fun sendMessage(chatId: Long, text: String)
    fun sendWeather(chatId: Long, weather: Weather, type: Weather.Type)
    fun sendWeatherForecast(chatId: Long, forecast: List<Weather>)
}