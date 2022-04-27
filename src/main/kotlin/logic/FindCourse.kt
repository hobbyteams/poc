package logic

import com.google.protobuf.Timestamp
import com.google.protobuf.timestamp
import com.kanekto.model.v1.Course
import model.course
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import java.time.Instant

// todo find a home
fun Instant.toTimestamp(): Timestamp = timestamp { seconds = epochSecond; nanos = nano }
fun Timestamp.toInstant(): Instant = Instant.ofEpochSecond(seconds, nanos.toLong())

val DATABASE_HOST = System.getenv("DATABASE_HOST") ?: "localhost"
val DATABASE_NAME = System.getenv("DATABASE_NAME") ?: "ht"
val DATABASE_USER: String = System.getenv("DATABASE_USER")
val DATABASE_PASSWORD: String = System.getenv("DATABASE_PASSWORD")

fun findCourse(id: Int): Course {
    println("Searching for $id")

    // do a database search...
    val database = Database.connect("jdbc:postgresql://$DATABASE_HOST/$DATABASE_NAME",
        user = DATABASE_USER, password = DATABASE_PASSWORD)

    return database.from(model.Course).select()
        .where { (model.Course.id eq id) }
        .map { it.course() }
        .first()
}
