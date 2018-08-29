package app.telegram.bot

import app.telegram.bot.business.implementation.BotInteractorImpl
import app.telegram.bot.business.implementation.ChatManagerImpl
import app.telegram.bot.business.implementation.UpdateHandlerImpl
import app.telegram.bot.business.inheritence.BotInteractor
import app.telegram.bot.business.inheritence.ChatManager
import app.telegram.bot.data.api.habr.HabrApi
import app.telegram.bot.data.api.habr.HabrSiteParser
import app.telegram.bot.data.api.habr.PostApi
import app.telegram.bot.data.api.yahoo.WeatherApi
import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.data.service.post.PostServiceImpl
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.data.service.weather.WeatherServiceImpl
import app.telegram.bot.data.storage.dao.DaoStorage
import app.telegram.bot.data.storage.dao.SentPostRepository
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
    @Bean fun user() = CurrentUser()

    @Bean fun updateHandler(currentUser: CurrentUser, interactor: BotInteractor) = UpdateHandlerImpl(currentUser, interactor)

    @Bean fun botInteractor(currentUser: CurrentUser,
                            chatManager: ChatManager,
                            weatherService: WeatherService,
                            postService: PostService): BotInteractor {
        return BotInteractorImpl(currentUser, chatManager, weatherService, postService)
    }

    @Bean fun chatManager(bot: TelegramBot) = ChatManagerImpl(bot)

    @Bean fun weatherApi(): WeatherApi = retrofit(WeatherApi.baseUrl).create(WeatherApi::class.java)
    @Bean fun postApi(): PostApi = HabrApi(HabrSiteParser())

    private fun retrofit(baseUrl: String) = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
}