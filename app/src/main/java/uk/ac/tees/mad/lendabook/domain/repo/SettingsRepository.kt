package uk.ac.tees.mad.lendabook.domain.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.domain.model.AppSettings

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun updateNotifications(enabled: Boolean)
    suspend fun updateDarkMode(enabled: Boolean)
}