package ch.wenksi.pushalerts.ui.tasks

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * This class serves as an adapter for the TabView to select the correct fragment
 */
class TabLayoutFragmentAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> TabClosedFragment()
            else -> TabOpenFragment()
        }
    }
}
