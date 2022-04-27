package model

import logic.toTimestamp
import org.ktorm.database.Database
import org.ktorm.dsl.QueryRowSet
import org.ktorm.dsl.eq
import org.ktorm.dsl.from
import org.ktorm.dsl.map
import org.ktorm.dsl.select
import org.ktorm.dsl.where
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.timestamp
import org.ktorm.schema.varchar
import java.time.Instant

object Course : Table<Nothing>("course") {
    val id = int("id").primaryKey()
    val name = varchar("name")
    val createdAt = timestamp("created_at")
}

fun QueryRowSet.course(): com.kanekto.model.v1.Course {
    val row = this
    return com.kanekto.model.v1.course {
        this.id = row[Course.id]!!.toLong()
        this.name = row[Course.name]!!
        this.createdDate = row[Course.createdAt]!!.toTimestamp()
    }
}
