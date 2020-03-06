package alistar.app.treasury

import alistar.app.databinding.TreasuryListItemBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter : RecyclerView.Adapter<Adapter.Holder>() {

    private val items = ArrayList<TreasuryReceipt>()

    class Holder(private val binding: TreasuryListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(treasuryReceipt: TreasuryReceipt) {
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TreasuryListItemBinding.inflate(layoutInflater, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])
    }

}