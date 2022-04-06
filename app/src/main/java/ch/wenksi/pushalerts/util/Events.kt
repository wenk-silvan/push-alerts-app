package ch.wenksi.pushalerts.util

import androidx.lifecycle.MutableLiveData
import ch.wenksi.pushalerts.models.Notification

object Events {
    val newNotification: MutableLiveData<Notification> by lazy {
        MutableLiveData<Notification>()
    }
}