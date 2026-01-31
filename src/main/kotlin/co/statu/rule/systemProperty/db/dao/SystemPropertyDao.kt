package co.statu.rule.systemProperty.db.dao

import co.statu.rule.database.Dao
import co.statu.rule.systemProperty.db.model.SystemProperty
import io.vertx.jdbcclient.JDBCPool
import io.vertx.sqlclient.Pool

abstract class SystemPropertyDao : Dao<SystemProperty>(SystemProperty::class) {
    abstract suspend fun add(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    )

    abstract suspend fun update(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    )

    abstract suspend fun isPropertyExists(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ): Boolean

    abstract suspend fun getValue(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ): SystemProperty?

    abstract suspend fun getAll(
        jdbcPool: Pool
    ): List<SystemProperty>
}