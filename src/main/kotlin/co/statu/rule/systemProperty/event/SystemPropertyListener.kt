package co.statu.rule.systemProperty.event

import co.statu.parsek.api.PluginEvent
import co.statu.rule.systemProperty.db.dao.SystemPropertyDao

interface SystemPropertyListener : PluginEvent {
    suspend fun onReady(systemPropertyDao: SystemPropertyDao)
}