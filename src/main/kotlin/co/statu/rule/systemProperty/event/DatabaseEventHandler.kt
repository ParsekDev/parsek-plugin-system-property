package co.statu.rule.systemProperty.event

import co.statu.parsek.api.annotation.EventListener
import co.statu.rule.database.DatabaseManager
import co.statu.rule.database.event.DatabaseEventListener
import co.statu.rule.systemProperty.SystemPropertyPlugin

@EventListener
class DatabaseEventHandler(private val systemPropertyPlugin: SystemPropertyPlugin) : DatabaseEventListener {
    override suspend fun onReady(databaseManager: DatabaseManager) {
        databaseManager.initialize(systemPropertyPlugin, systemPropertyPlugin)
    }
}