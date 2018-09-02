package app.telegram.bot.data.storage.hibernate

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    @Query("select * from user where chat_id = ?1 LIMIT 1", nativeQuery = true)
    fun findByChat(chatId: Long): Optional<User>
}