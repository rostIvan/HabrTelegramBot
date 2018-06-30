package app.telegram.bot.business.inheritence

import com.pengrad.telegrambot.model.Update

interface UpdateHandler {
    fun onUpdateReceive(update: Update)
}