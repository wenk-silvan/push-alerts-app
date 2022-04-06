package ch.wenksi.pushalerts.services.notifications

import android.util.Log
import ch.wenksi.pushalerts.models.Notification
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.util.Events
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

const val TAG: String = "AppFirebaseMessagingService"

class AppFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        /**
         * Subscribes to topic for each project name
         * @param projects: The projects to subscribe to
         */
        fun subscribeToNotifications(projects: List<Project>) {
            projects.forEach { project ->
                Firebase.messaging.subscribeToTopic(project.name)
                    .addOnCompleteListener { task ->
                        Log.e(
                            TAG,
                            "${if (task.isSuccessful) "Subscribed" else "Couldn't subscribe"} to ${project.name}"
                        )
                    }
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "The token refreshed: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        var title = ""
        var body = ""
        var payload: Map<String, String>? = null

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            payload = remoteMessage.data
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Title: ${it.title}")
            Log.d(TAG, "Message Notification Body: ${it.body}")
            title = if (it.title == null) "" else it.title!!
            body = if (it.body == null) "" else it.body!!
        }

        Events.newNotification.postValue(Notification(title, body, payload))
    }
}