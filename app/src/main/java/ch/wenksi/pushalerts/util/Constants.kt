package ch.wenksi.pushalerts.util

object Constants {
    const val apiTimeout: Long = 5_000
    const val apiBaseUrl = "http://10.0.2.2:5032/api/"
    const val apiProjectsUri = "projects"
    const val apiTasksUri = "tasks"
    const val apiLoginUri = "login"

    // Shared Preferences Keys
    const val PREFS_TOKEN_VALUE = "TOKEN_VALUE"
    const val PREFS_TOKEN_EXPIRY = "TOKEN_EXPIRY"
}