package co.statu.rule.systemProperty.db.model

import co.statu.rule.database.DBEntity
import java.util.*

data class SystemProperty(
    val id: UUID? = UUID.randomUUID(),
    val option: String,
    val value: String = ""
) : DBEntity()