package uk.ac.tees.mad.lendabook.data.repo

import android.content.Context
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.lendabook.data.local.SettingsDataStore
import uk.ac.tees.mad.lendabook.domain.model.AppSettings
import uk.ac.tees.mad.lendabook.domain.repo.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val context: Context
) : SettingsRepository {

    override fun getSettings(): Flow<AppSettings> = SettingsDataStore.getSettings(context)

    override suspend fun updateNotifications(enabled: Boolean) =
        SettingsDataStore.setNotificationsEnabled(context, enabled)

    override suspend fun updateDarkMode(enabled: Boolean) =
        SettingsDataStore.setDarkModeEnabled(context, enabled)
}