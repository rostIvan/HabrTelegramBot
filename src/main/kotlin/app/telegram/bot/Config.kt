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
import app.telegram.bot.data.service.post.PostService
import app.telegram.bot.data.service.weather.WeatherService
import app.telegram.bot.util.retrofit
import app.telegram.bot.util.create
import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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

    @Bean fun postApi(): PostApi = HabrApi(HabrSiteParser())
    @Bean fun weatherApi(): WeatherApi = retrofit(WeatherApi.baseUrl).create(WeatherApi::class)
}