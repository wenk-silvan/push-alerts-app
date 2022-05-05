package ch.wenksi.pushalerts.services.notifications

import android.util.Log
import ch.wenksi.pushalerts.models.Notification
import ch.wenksi.pushalerts.models.Project
import ch.wenksi.pushalerts.util.Events
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

/**
 * This class acts as a service to communicate with Firebase Could Messaging.
 * Make sure that the google-services.json file exists and add this service to the AndroidManifest.
 */
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
                        Log.i(
                            AppFirebaseMessagingService::class.qualifiedName,
                            "${if (task.isSuccessful) "Subscribed" else "Couldn't subscribe"} to ${project.name}"
                        )
                    }
            }
        }
    }

    /**
     * Logs a newly registered Firebase Cloud Messaging service instance
     */
    override fun onNewToken(token: String) {
        Log.d(
            AppFirebaseMessagingService::class.qualifiedName, "The token refreshed: $token"
        )
    }

    /**
     * Called when a the app receives a new message from Firebase Cloud Messaging.
     * After some validation the Events.newNotification live data gets triggered.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(
            AppFirebaseMessagingService::class.qualifiedName, "From: ${remoteMessage.from}"
        )

        var title = ""
        var body = ""
        var payload: Map<String, String>? = null

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(
                AppFirebaseMessagingService::class.qualifiedName,
                "Message data payload: ${remoteMessage.data}"
            )
            payload = remoteMessage.data
        }

        remoteMessage.notification?.let {
            Log.d(
                AppFirebaseMessagingService::class.qualifiedName,
                "Message Notification Title: ${it.title}"
            )
            Log.d(
                AppFirebaseMessagingService::class.qualifiedName,
                "Message Notification Body: ${it.body}"
            )
            title = if (it.title == null) "" else it.title!!
            body = if (it.body == null) "" else it.body!!
        }

        Events.newNotification.postValue(Notification(title, body, payload))
    }
}