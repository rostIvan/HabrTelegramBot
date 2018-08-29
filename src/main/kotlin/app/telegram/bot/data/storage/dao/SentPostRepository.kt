package app.telegram.bot.data.storage.dao

import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.model.PostDTO
import org.springframework.stereotype.Repository

@Repository
class SentPostRepository(private val user: CurrentUser) : DaoStorage<PostDTO> {

    override fun findFirst(): PostDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findLast(): PostDTO {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): List<PostDTO> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun save(t: PostDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(t: PostDTO) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveAll(iterable: Iterable<PostDTO>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll(iterable: Iterable<PostDTO>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun count(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isEmpty(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(t: PostDTO): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun id() = user.chatId()
}