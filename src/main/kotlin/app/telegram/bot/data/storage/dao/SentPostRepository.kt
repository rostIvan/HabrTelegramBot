package app.telegram.bot.data.storage.dao

import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.storage.hibernate.PostDbModel
import app.telegram.bot.data.storage.hibernate.User
import app.telegram.bot.data.storage.hibernate.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
class SentPostRepository(private val currentUser: CurrentUser) : DaoStorage<PostDbModel> {

    @Autowired private lateinit var userRepository: UserRepository

    override fun findFirst(): PostDbModel = receivedPosts().first()
    override fun findLast(): PostDbModel  = receivedPosts().last()
    override fun findAll(): List<PostDbModel> = receivedPosts()

    @Transactional
    override fun save(t: PostDbModel) {
        receivedPosts().add(t)
    }

    @Transactional
    override fun delete(t: PostDbModel) {
        receivedPosts().remove(t)
    }

    @Transactional
    override fun saveAll(iterable: Iterable<PostDbModel>) {
        receivedPosts().addAll(iterable)
    }

    @Transactional
    override fun deleteAll(iterable: Iterable<PostDbModel>) {
        receivedPosts().removeAll(iterable)
    }

    @Transactional
    override fun deleteAll() {
        receivedPosts().clear()
    }

    override fun count(): Int = receivedPosts().size
    override fun isEmpty(): Boolean = receivedPosts().isEmpty()
    override fun contains(t: PostDbModel): Boolean = receivedPosts().contains(t)
    override fun containsAll(iterable: Iterable<PostDbModel>): Boolean = receivedPosts().containsAll(iterable.toList())

    private fun receivedPosts() = findOrSaveUser().receivedPosts
    private fun findOrSaveUser() = userRepository
            .findByChat(id())
            .orElseGet { userRepository.save(User(chatId = id(), name = name())) }

    private fun id() = currentUser.chatId()
    private fun name() = currentUser.nickname()
}