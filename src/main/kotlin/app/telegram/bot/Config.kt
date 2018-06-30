package app.telegram.bot

import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.implementation.UpdateHandlerImpl
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Configuration
class Config {
    @Bean fun bot(@Value("\${bot.api-key}") accessToken: String) = TelegramBot(accessToken)
    @Bean fun botInteractor(bot: TelegramBot) = BotInteractorImpl(bot)
    @Bean fun updateHandler(interactor: BotInteractor) = UpdateHandlerImpl(interactor)
    @Bean fun weatherApi(@Value("\${weather.base-url}") weatherUrl: String): WeatherApi = retrofit(weatherUrl).create(WeatherApi::class.java)

    private fun retrofit(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}