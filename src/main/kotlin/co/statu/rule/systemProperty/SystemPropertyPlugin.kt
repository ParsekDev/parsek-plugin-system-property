package co.statu.rule.systemProperty

import co.statu.parsek.api.ParsekPlugin
import co.statu.rule.database.DatabaseManager
import org.springframework.beans.factory.getBean

class SystemPropertyPlugin : ParsekPlugin() {
    override suspend fun onStart() {
        val databaseManager = pluginGlobalBeanContext.beanFactory.getBean<DatabaseManager>()

        databaseManager.initialize(this)
    }
}

