package app.telegram.bot.unit.data.storage

import app.telegram.bot.data.model.CurrentUser
import app.telegram.bot.data.model.PostDTO
import app.telegram.bot.data.storage.dao.SentPostRepository
import app.telegram.bot.data.storage.hibernate.PostDbModel
import app.telegram.bot.data.storage.hibernate.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner


@RunWith(SpringJUnit4ClassRunner::class)
@DataJpaTest
@Import(SentPostRepository::class)
class SentPostRepositoryTest {
    @MockBean lateinit var currentUser: CurrentUser
    @Autowired lateinit var userRepository: UserRepository
    @Autowired lateinit var postRepository: SentPostRepository

    @Before fun setUp() {
        `when`(currentUser.chatId()).thenReturn(2000)
        `when`(currentUser.nickname()).thenReturn("Kelly")
    }

    @Test fun findAll_shouldReturnSavedPosts() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Rust", "http://habr.com/post1125", "Empty", listOf("Rust", "Rust lang", "Programming"), "qiwi1315", "Now")
        val post3 = PostDbModel(0L, "C++", "http://habr.com/post23516", "Empty", listOf("C++", "std"), "lew", "Now")
        postRepository.saveAll(listOf(post1, post2, post3))

        val posts = postRepository.findAll()
        val users = userRepository.findAll()
        assertThat(posts).hasSize(3).containsAll(listOf(post1, post2, post3))
        assertThat(users).isNotEmpty.hasSize(1)
        users.first().let { assertThat(it.name == "Kelly" && it.chatId == 2000L).isTrue() }
    }

    @Test fun save_shouldSavePost() {
        val post = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        postRepository.save(post)
        val first = postRepository.findFirst()
        assertThat(post).isEqualTo(first)
    }

    @Test fun delete_shouldDeletePost() {
        val post = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        postRepository.save(post)
        postRepository.delete(post)
        assertThat(postRepository.findAll()).isEmpty()
    }

    @Test fun addAll_shouldDeletePosts() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        postRepository.saveAll(listOf(post1, post2))
        assertThat(postRepository.findAll())
                .isNotEmpty
                .hasSize(2)
                .containsAll(listOf(post1, post2))
    }

    @Test fun deleteAll_withArgs_shouldDeletePosts() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        postRepository.saveAll(listOf(post1, post2))
        postRepository.deleteAll(listOf(post1, post2))
        assertThat(postRepository.findAll()).isEmpty()
        assertThat(postRepository.isEmpty()).isTrue()
    }

    @Test fun deleteAll_withoutArgs_shouldDeleteAllPosts() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        postRepository.saveAll(listOf(post1, post2))
        assertThat(postRepository.findAll()).hasSize(2)
        postRepository.deleteAll(listOf(post1, post2))
        assertThat(postRepository.findAll()).isEmpty()
        assertThat(postRepository.isEmpty()).isTrue()
    }

    @Test fun contains_shouldReturnValidBoolean() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        val post3 = PostDbModel(0L, "C++", "http://habr.com/post23516", "Empty", listOf("C++", "std"), "lew", "Now")
        postRepository.save(post1)
        assertThat(postRepository.contains(post1)).isTrue()
        assertThat(postRepository.contains(post2)).isFalse()
        postRepository.save(post3)
        assertThat(postRepository.contains(post3)).isTrue()
    }

    @Test fun containsAll_shouldReturnValidBoolean() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        val post3 = PostDbModel(0L, "C++", "http://habr.com/post23516", "Empty", listOf("C++", "std"), "lew", "Now")
        val post4 = PostDbModel(0L, "A", "http://habr.com/post2335", "Empty", listOf(), "Alisa", "Now")
        val post5 = PostDbModel(0L, "B", "http://habr.com/post2335", "Empty", listOf(), "Jack", "Now")
        postRepository.saveAll(listOf(post1, post2, post3))
        assertThat(postRepository.containsAll(listOf(post1, post2, post3))).isTrue()
        assertThat(postRepository.containsAll(listOf(post4, post5))).isFalse()
        assertThat(postRepository.containsAll(listOf(post1, post5))).isFalse()
        assertThat(postRepository.containsAll(listOf(post2, post4))).isFalse()
    }

    @Test fun isEmpty_shouldReturnValidBoolean() {
        assertThat(postRepository.isEmpty()).isTrue()
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        postRepository.save(post1)
        assertThat(postRepository.isEmpty()).isFalse()
        postRepository.save(post2)
        assertThat(postRepository.isEmpty()).isFalse()
        postRepository.deleteAll()
        assertThat(postRepository.isEmpty()).isTrue()
    }

    @Test fun firsAndLast_shouldReturnUser() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        val post3 = PostDbModel(0L, "C++", "http://habr.com/post23516", "Empty", listOf("C++", "std"), "lew", "Now")
        postRepository.saveAll(listOf(post1, post2, post3))
        assertThat(postRepository.findFirst()).isEqualTo(post1)
        assertThat(postRepository.findLast()).isEqualTo(post3)
    }

    @Test fun count_shouldReturnValidInt() {
        val post1 = PostDbModel(0L, "Hello world", "http://habr.com/post23151", "Empty", listOf(), "Kesha", "Now")
        val post2 = PostDbModel(0L, "Abc", "http://habr.com/post23125", "Empty", listOf(), "Lola", "Now")
        val post3 = PostDbModel(0L, "C++", "http://habr.com/post23516", "Empty", listOf("C++", "std"), "lew", "Now")
        postRepository.save(post1)
        assertThat(postRepository.count()).isEqualTo(1)
        postRepository.save(post2)
        assertThat(postRepository.count()).isEqualTo(2)
        postRepository.save(post3)
        assertThat(postRepository.count()).isEqualTo(3)
    }
}