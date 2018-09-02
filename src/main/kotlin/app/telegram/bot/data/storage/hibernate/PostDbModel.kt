package app.telegram.bot.data.storage.hibernate

import app.telegram.bot.data.model.PostDTO
import org.hibernate.annotations.Type
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "post")
data class PostDbModel (
        @Id @GeneratedValue @Column(name = "id")
        val id: Long = 0L,

        @Column(name = "title") @get: NotBlank
        var title: String = "",

        @Column(name = "link") @get: NotBlank
        var link: String = "",

        @Column(name = "description")
        @Type(type="text")
        var description: String = "",

        @ElementCollection(targetClass = String::class, fetch = FetchType.LAZY) @CollectionTable(name = "user_post_tags")
        @Column(name = "tags")
        var tags: List<String> = mutableListOf(),

        @Column(name = "postByUser")
        var postByUser: String = "",

        @Column(name = "date")
        var date: String = ""
) {
    fun toDTO() = PostDTO(title, link, description, tags.toList(), postByUser, date)
}