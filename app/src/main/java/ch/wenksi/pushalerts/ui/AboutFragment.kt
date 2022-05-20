package ch.wenksi.pushalerts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.databinding.FragmentAboutBinding
import ch.wenksi.pushalerts.models.License
import ch.wenksi.pushalerts.models.Task
import ch.wenksi.pushalerts.ui.tasks.ClosedTasksAdapter

/**
 * The about screen to display licences and contact information
 */
class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerViewAdapter: LicensesAdapter
    private val description = "This App is part of the Push Alerts System and provides a solution to efficiently manage events or urgent tasks within a team. Tasks are created automatically through external systems and users of the according project get notified and reminded through push-notifications. Assign yourself to a task and mark it as Done or Rejected when finished."
    private val licenses = listOf(
        License(
            "AndroidX",
            "Core platform to develop android apps\ndeveloper.android.com/jetpack/androidx#api_reference",
            "Apache 2.0 License"
        ),
        License(
            "Material Design 2",
            "Design framework of Google\nmaterial.io",
            "Apache 2.0 License"
        ),
        License(
            "Square Retrofit 2",
            "Library to create type-safe http clients for Android and Java\nsquare.github.io/retrofit/",
            "Apache 2.0 License"
        ),
        License(
            "Square OkHttp 3",
            "Library http middlewares and mocking server\nsquare.github.io/okhttp/",
            "Apache 2.0 License"
        ),
        License(
            "FCM",
            "Firebase Cloud Messaging is a library to communicate with Firebase's push-notification service\nfirebase.google.com/docs/cloud-messaging/",
            "Apache 2.0 License"
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvDescription.text = description
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        recyclerViewAdapter = LicensesAdapter(licenses)
        binding.rvLicenses.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvLicenses.adapter = recyclerViewAdapter
        binding.rvLicenses.isNestedScrollingEnabled = false
        binding.rvLicenses.addItemDecoration(
            DividerItemDecoration(activity, DividerItemDecoration.VERTICAL)
        )
    }
}
