package app.telegram.bot.data.storage.hibernate

import javax.persistence.*

@Entity
@Table(name = "user")
data class User (
        @Id @Column(name = "id") val id: Long = 0,
        @Column(name = "name") var name: String,
        @Column(name = "location") var location: String,

        @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) @JoinColumn(name = "user_id")
        @Column(name = "received_posts") var receivedPosts: List<PostDbModel>
)