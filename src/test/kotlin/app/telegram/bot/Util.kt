package app.telegram.bot

import org.mockito.Mockito

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)

@Suppress("DEPRECATION")
fun <T> anyObj(): T = Mockito.anyObject<T>()
