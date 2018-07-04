package app.telegram.bot.business.inheritence

import java.lang.reflect.ParameterizedType

abstract class AbstractMessagePreparation<T> {
    abstract fun toMessage(t: T) : String
    abstract fun toMessage(list: List<T>) : String

    @SuppressWarnings("unchecked")
    fun clazz() : Class<T> = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>
}