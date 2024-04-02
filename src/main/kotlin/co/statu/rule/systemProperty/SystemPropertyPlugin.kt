package co.statu.rule.systemProperty

import co.statu.parsek.api.ParsekPlugin
import co.statu.rule.database.Dao
import co.statu.rule.database.DatabaseMigration
import co.statu.rule.database.api.DatabaseHelper
import co.statu.rule.systemProperty.db.impl.SystemPropertyDaoImpl

class SystemPropertyPlugin : ParsekPlugin(), DatabaseHelper {
    override val tables: List<Dao<*>> = listOf(SystemPropertyDaoImpl())

    override val migrations: List<DatabaseMigration> = listOf()
}

