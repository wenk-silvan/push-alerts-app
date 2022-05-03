package ch.wenksi.pushalerts.util

import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.models.Notification

object Events {
    /**
     * This live data property is used to trigger an action when onMessageReceived is executed in the AppFirebaseMessagingService.
     */
    val newNotification: MutableLiveData<Notification> by lazy {
        MutableLiveData<Notification>()
    }
}