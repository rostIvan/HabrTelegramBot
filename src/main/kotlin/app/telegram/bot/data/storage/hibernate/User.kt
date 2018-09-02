package app.telegram.bot.data.storage.hibernate

import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user")
data class User(
        @Id @GeneratedValue @Column(name = "id")
        val id: Long = 0,

        @Column(name = "chat_id")
        var chatId: Long = 0,

        @Column(name = "name") @get: NotBlank
        var name: String = "",

        @Column(name = "location")
        var location: String = "",

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER) @JoinColumn(name = "user_id")
        @Column(name = "received_posts")
        var receivedPosts: MutableList<PostDbModel> = mutableListOf()
)