package app.telegram.bot.data.storage.dao

interface DaoStorage<T> {
    fun findFirst() : T
    fun findLast() : T
    fun findAll() : List<T>

    fun save(t: T)
    fun delete(t: T)

    fun saveAll(iterable: Iterable<T>)
    fun deleteAll(iterable: Iterable<T>)
    fun deleteAll()

    fun count() : Int
    fun isEmpty(): Boolean
    fun contains(t: T): Boolean
}