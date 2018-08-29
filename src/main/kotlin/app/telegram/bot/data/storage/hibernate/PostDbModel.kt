package app.telegram.bot.data.storage.hibernate

import app.telegram.bot.data.model.PostDTO
import javax.persistence.*

@Entity
@Table(name = "post")
data class PostDbModel (
        @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id") val id: Long = 0,
        @Column(name = "title") var title: String,
        @Column(name = "link") var link: String,
        @Column(name = "description") var description: String,
        @ElementCollection(targetClass = String::class) @Column(name = "tags") var tags: List<String>,
        @Column(name = "postByUser") var postByUser: String,
        @Column(name = "date") var date: String
) {
    fun toPostDTO() = PostDTO(title, link, description, tags, postByUser, date)
}