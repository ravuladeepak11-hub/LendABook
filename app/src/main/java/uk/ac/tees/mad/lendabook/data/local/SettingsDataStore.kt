package uk.ac.tees.mad.lendabook.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.lendabook.domain.model.AppSettings

private val Context.dataStore by preferencesDataStore("lendabook_settings")

object SettingsDataStore {
    private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    private val DARK_MODE_ENABLED = booleanPreferencesKey("dark_mode_enabled")

    suspend fun setNotificationsEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[NOTIFICATIONS_ENABLED] = enabled }
    }

    suspend fun setDarkModeEnabled(context: Context, enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE_ENABLED] = enabled }
    }

    fun getSettings(context: Context) = context.dataStore.data.map {
        AppSettings(
            notificationsEnabled = it[NOTIFICATIONS_ENABLED] ?: true,
            darkModeEnabled = it[DARK_MODE_ENABLED] ?: false
        )
    }
}