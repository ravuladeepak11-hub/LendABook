package uk.ac.tees.mad.lendabook.domain.model

data class AppSettings(
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)