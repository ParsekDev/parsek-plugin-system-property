package co.statu.rule.systemProperty.db.dao

import co.statu.rule.database.Dao
import co.statu.rule.systemProperty.db.model.SystemProperty
import io.vertx.jdbcclient.JDBCPool

abstract class SystemPropertyDao : Dao<SystemProperty>(SystemProperty::class) {
    abstract suspend fun add(
        systemProperty: SystemProperty,
        jdbcPool: JDBCPool
    )

    abstract suspend fun update(
        systemProperty: SystemProperty,
        jdbcPool: JDBCPool
    )

    abstract suspend fun isPropertyExists(
        systemProperty: SystemProperty,
        jdbcPool: JDBCPool
    ): Boolean

    abstract suspend fun getValue(
        systemProperty: SystemProperty,
        jdbcPool: JDBCPool
    ): SystemProperty?

    abstract suspend fun getAll(
        jdbcPool: JDBCPool
    ): List<SystemProperty>
}