package co.statu.rule.systemProperty.event

import co.statu.rule.database.Dao.Companion.get
import co.statu.rule.database.DatabaseManager
import co.statu.rule.database.event.DatabaseEventListener
import co.statu.rule.systemProperty.SystemPropertyPlugin
import co.statu.rule.systemProperty.db.dao.SystemPropertyDao

class DatabaseEventHandler : DatabaseEventListener {
    private val systemPropertyDao by lazy {
        get<SystemPropertyDao>(SystemPropertyPlugin.tables)
    }

    override suspend fun onReady(databaseManager: DatabaseManager) {
        databaseManager.migrateNewPluginId(
            "system-property",
            SystemPropertyPlugin.INSTANCE.context.pluginId,
            SystemPropertyPlugin.INSTANCE
        )

        databaseManager.initialize(
            SystemPropertyPlugin.INSTANCE,
            SystemPropertyPlugin.tables,
            SystemPropertyPlugin.migrations,
        )

        val handlers =
            SystemPropertyPlugin.INSTANCE.context.pluginEventManager.getEventHandlers<SystemPropertyListener>()

        handlers.forEach {
            it.onReady(systemPropertyDao)
        }
    }
}