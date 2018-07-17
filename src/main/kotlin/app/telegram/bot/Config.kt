package app.telegram.bot

import app.telegram.bot.api.habr.HabrApi
import app.telegram.bot.api.habr.PostApi
import app.telegram.bot.api.yahoo.WeatherApi
import app.telegram.bot.business.implementation.*
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.business.inheritence.PostProvider
import app.telegram.bot.business.inheritence.WeatherProvider
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

    @Bean fun weatherApi(@Value("\${weather.base-url}") weatherUrl: String): WeatherApi = retrofit(weatherUrl).create(WeatherApi::class.java)
    @Bean fun postApi(@Value("\${weather.base-url}") weatherUrl: String): PostApi = HabrApi()

    @Bean fun weatherProvider(weatherApi: WeatherApi) = WeatherProviderImpl(weatherApi)
    @Bean fun postProvider(postApi: PostApi) = PostProviderImpl(postApi)

    @Bean fun chatManager(bot: TelegramBot) = ChatManagerImpl(bot)

    @Bean fun botInteractor(chatManager: ChatManager,
                            weatherProvider: WeatherProvider,
                            postProvider: PostProvider) = BotInteractorImpl(chatManager, weatherProvider, postProvider)

    @Bean fun updateHandler(interactor: BotInteractor) = UpdateHandlerImpl(interactor)

    private fun retrofit(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}