package app.telegram.bot.unit.data.storage

import app.telegram.bot.data.storage.hibernate.User
import app.telegram.bot.data.storage.hibernate.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.assertj.core.api.Condition
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import reactor.core.publisher.toMono


@RunWith(SpringJUnit4ClassRunner::class)
@DataJpaTest
class UserRepositoryTest {
    @Autowired private lateinit var userRepository: UserRepository

    @Test fun save_shouldInsertUser() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        userRepository.save(user1)
        assertThat(userRepository.findAll()).hasSize(1)
        userRepository.save(user2)
        val all = userRepository.findAll()
        assertThat(all).hasSize(2)
        assertThat(all[0]).isEqualTo(user1)
        assertThat(all[1]).isEqualTo(user2)
    }

    @Test fun delete_shouldRemoveUser() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        userRepository.save(user1)
        userRepository.save(user2)
        assertThat(userRepository.findAll()).hasSize(2)
        userRepository.delete(user1)
        val all = userRepository.findAll()
        assertThat(all).hasSize(1)
        assertThat(all[0]).isEqualTo(user2)
        userRepository.delete(user2)
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test fun addAll_shouldAddUsers() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        assertThat(userRepository.findAll()).hasSize(3)
        assertThat(userRepository.findAll()).containsAll(listOf(user1, user2, user3))
    }

    @Test fun deleteAll_withArgs_shouldRemoveUsers() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        userRepository.deleteAll(listOf(user2, user3))
        assertThat(userRepository.findAll()).hasSize(1)
        assertThat(userRepository.findAll()).contains(user1)
    }

    @Test fun deleteAll_withoutArgs_shouldRemoveAllUsers() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        userRepository.deleteAll()
        assertThat(userRepository.findAll()).isEmpty()
    }

    @Test fun count_shouldReturnNumberOfUsers() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        assertThat(userRepository.count()).isEqualTo(3)
        userRepository.deleteAll()
        assertThat(userRepository.count()).isEqualTo(0)
    }

    @Test fun findByChatId_shouldReturnValidUser() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        val user = userRepository.findByChat(user2.chatId).get()
        assertThat(user).isEqualTo(user2)
    }

    @Test fun findByChatId_ifNotExist_shouldReturnNull() {
        val user1 = User(name = "John", chatId = 10014, location = "London")
        val user2 = User(name = "Alex", chatId = 22014, location = "Los angeles")
        val user3 = User(name = "Andrew", chatId = 53120, location = "San-Francisco")
        userRepository.saveAll(listOf(user1, user2, user3))
        userRepository.findByChat(100).ifPresent { fail("Element not exist, but exception don't throw") }
    }
}