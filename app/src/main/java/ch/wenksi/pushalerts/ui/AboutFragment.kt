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
    private val licenses = listOf(
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
        License("aafsd", "adfasd", "asdfasdf"),
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
