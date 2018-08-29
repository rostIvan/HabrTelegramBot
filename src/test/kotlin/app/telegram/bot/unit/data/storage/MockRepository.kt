package app.telegram.bot.unit.data.storage

import app.telegram.bot.data.storage.dao.DaoStorage

class MockRepository<T> : DaoStorage<T> {
    private val mockedList = mutableListOf<T>()

    override fun findFirst(): T = mockedList.first()
    override fun findLast(): T = mockedList.last()
    override fun findAll(): List<T> = mockedList

    override fun save(t: T) { mockedList.add(t) }
    override fun delete(t: T) { mockedList.remove(t) }
    override fun saveAll(iterable: Iterable<T>) { mockedList.addAll(iterable) }
    override fun deleteAll(iterable: Iterable<T>) { mockedList.removeAll(iterable) }
    override fun deleteAll() { mockedList.clear() }

    override fun count(): Int = mockedList.size

    override fun isEmpty(): Boolean  = mockedList.isEmpty()

    override fun contains(t: T): Boolean = mockedList.contains(t)
}
