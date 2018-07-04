package app.telegram.bot.business.implementation

import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.Weather

class ChatManagerImpl : ChatManager {
    override fun sendMessage(chatId: Long, text: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendWeather(chatId: Long, weather: Weather, type: Weather.Type) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendWeatherForecast(chatId: Long, forecast: List<Weather>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}