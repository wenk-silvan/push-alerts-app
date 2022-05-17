package ch.wenksi.pushalerts.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.wenksi.pushalerts.R
import ch.wenksi.pushalerts.databinding.ItemLicenseBinding
import ch.wenksi.pushalerts.models.License

/**
 * This class is used for the RecyclerView to display the list of licenses in the AboutFragment.
 * It binds the license data to the a list entry.
 */
class LicensesAdapter(private val licenses: List<License>) :
    RecyclerView.Adapter<LicensesAdapter.ViewHolder>() {

    private lateinit var context: Context

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = ItemLicenseBinding.bind(itemView)

        fun databind(license: License) {
            binding.tvLicenseName.text = license.name
            binding.tvLicenseText.text = license.text
            binding.tvLicenseVersion.text = license.version
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_license, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return licenses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(licenses[position])
    }
}
