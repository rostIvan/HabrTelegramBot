package app.telegram.bot.util

import java.util.*

fun ClosedRange<Int>.random() = Random().nextInt((endInclusive + 1) - start) +  start
fun <T> List<T>.random() = this[(0 until this.size).random()]
fun String.containsIgnoreCase(string: String) = this.contains(string, ignoreCase = true)

fun getStartMessage(nickname: String) = "Hello, $nickname, let's start! Use /help command for get start"

fun getHelpMessage() = """
    Commands:

    /start -> Send hello message
    /help -> Send this message
    /weather_current -> Send current weather in the selected region
    /weather_today -> Send today weather in the selected region
    /weather_tomorrow -> Send tomorrow weather in the selected region
    /weather_week -> Send week weather in the selected region
    /post_random -> Send random post from the top on post api site
    /posts_random -> Send random posts from the top on post api site
    /post_relevant -> Send relevant post from site api
    /posts_relevant -> Send relevant posts from site api

    Phrases:

    Send me ["query"] post -> Send me "Android" post
    Send me [number] posts -> Send me 10 posts
    Send me [number] ["query"] posts -> Send me 3 "Android" posts
""".trimIndent()
