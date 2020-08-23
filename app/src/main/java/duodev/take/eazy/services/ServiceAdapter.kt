package duodev.take.eazy.services

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.Service

class ServicesAdapter (
    private val list: MutableList<Service>
): BaseRecyclerViewAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_services, parent))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addData(data: List<Service>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val deliveryAddress: TextView = itemView.findViewById(R.id.deliveryAddress)
        private val storeAddress: TextView = itemView.findViewById(R.id.storeAddress)
        private val items: TextView = itemView.findViewById(R.id.items)

        fun bindItems(service: Service) {
            deliveryAddress.text = service.deliveryAddress
            storeAddress.text = service.storeAddress
            items.text = service.items
        }
    }

}
