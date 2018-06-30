package app.telegram.bot.controller

import app.telegram.bot.business.inheritence.UpdateHandler
import com.pengrad.telegrambot.BotUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class BotController(@Autowired val updateHandler: UpdateHandler) {

    @PostMapping("/webhook")
    fun webhook(@RequestBody updateBody: String) {
        val update = BotUtils.parseUpdate(updateBody)
        updateHandler.onUpdateReceive(update)
    }

}