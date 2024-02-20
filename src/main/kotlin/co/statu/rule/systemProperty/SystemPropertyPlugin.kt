package co.statu.rule.systemProperty

import co.statu.parsek.api.ParsekPlugin
import co.statu.parsek.api.PluginContext
import co.statu.rule.database.Dao
import co.statu.rule.database.DatabaseManager
import co.statu.rule.database.DatabaseMigration
import co.statu.rule.systemProperty.db.impl.SystemPropertyDaoImpl
import co.statu.rule.systemProperty.event.DatabaseEventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SystemPropertyPlugin(pluginContext: PluginContext) : ParsekPlugin(pluginContext) {
    companion object {
        internal val logger: Logger = LoggerFactory.getLogger(SystemPropertyPlugin::class.java)

        internal lateinit var INSTANCE: SystemPropertyPlugin

        internal val tables = listOf<Dao<*>>(SystemPropertyDaoImpl())
        internal val migrations = listOf<DatabaseMigration>()

        internal lateinit var databaseManager: DatabaseManager
    }

    init {
        INSTANCE = this

        logger.info("Initialized instance")

        context.pluginEventManager.register(this, DatabaseEventHandler())

        logger.info("Registered event")
    }
}

