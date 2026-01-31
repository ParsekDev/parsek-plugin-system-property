package co.statu.rule.systemProperty.db.impl

import co.statu.parsek.api.ParsekPlugin
import co.statu.rule.database.annotation.Dao
import co.statu.rule.systemProperty.SystemPropertyDefaults
import co.statu.rule.systemProperty.db.dao.SystemPropertyDao
import co.statu.rule.systemProperty.db.model.SystemProperty
import io.vertx.sqlclient.Pool
import io.vertx.kotlin.coroutines.*
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.Tuple
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Scope

@Dao
@Lazy
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
class SystemPropertyDaoImpl : SystemPropertyDao() {

    override suspend fun init(jdbcPool: Pool, plugin: ParsekPlugin) {
        jdbcPool
            .query(
                """
                        CREATE TABLE IF NOT EXISTS `${getTablePrefix() + tableName}` (
                            `id` UUID NOT NULL,
                            `option` String NOT NULL,
                            `value` String NOT NULL
                        ) ENGINE = MergeTree() order by `id`;
                    """
            )
            .execute()
            .coAwait()

        addWebsiteConfig(jdbcPool)
    }

    override suspend fun add(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ) {
        val query = "INSERT INTO `${getTablePrefix() + tableName}` (`id`, `option`, `value`) VALUES (?, ?, ?)"

        jdbcPool
            .preparedQuery(query)
            .execute(
                Tuple.of(
                    systemProperty.id,
                    systemProperty.option,
                    systemProperty.value
                )
            )
            .coAwait()
    }

    override suspend fun update(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ) {
        val params = Tuple.tuple()

        params.addString(systemProperty.value)

        if (systemProperty.id == null)
            params.addString(systemProperty.option)
        else
            params.addUUID(systemProperty.id)

        val query =
            "UPDATE `${getTablePrefix() + tableName}` SET value = ? ${if (systemProperty.id != null) ", option = ?" else ""} WHERE `${if (systemProperty.id == null) "option" else "id"}` = ?"

        jdbcPool
            .preparedQuery(query)
            .execute(
                params
            )
            .coAwait()
    }


    override suspend fun isPropertyExists(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ): Boolean {
        val query = "SELECT COUNT(`value`) FROM `${getTablePrefix() + tableName}` where `option` = ?"

        val rows: RowSet<Row> = jdbcPool
            .preparedQuery(query)
            .execute(
                Tuple.of(systemProperty.option)
            )
            .coAwait()

        return rows.toList()[0].getLong(0) != 0L
    }

    override suspend fun getValue(
        systemProperty: SystemProperty,
        jdbcPool: Pool
    ): SystemProperty? {
        val query = "SELECT `id`, `option`, `value` FROM `${getTablePrefix() + tableName}` where `option` = ?"

        val rows: RowSet<Row> = jdbcPool
            .preparedQuery(query)
            .execute(
                Tuple.of(
                    systemProperty.option
                )
            )
            .coAwait()

        if (rows.size() == 0) {
            return null
        }

        val row = rows.toList()[0]

        return row.toEntity()
    }

    override suspend fun getAll(jdbcPool: Pool): List<SystemProperty> {
        val query =
            "SELECT `id`, `option`, `value` FROM `${getTablePrefix() + tableName}`"

        val rows: RowSet<Row> = jdbcPool
            .preparedQuery(query)
            .execute()
            .coAwait()

        return rows.toEntities()
    }

    private suspend fun addWebsiteConfig(
        jdbcPool: Pool
    ) {
        SystemPropertyDefaults.entries.forEach {
            add(SystemProperty(option = it.name, value = it.value), jdbcPool)
        }
    }
}