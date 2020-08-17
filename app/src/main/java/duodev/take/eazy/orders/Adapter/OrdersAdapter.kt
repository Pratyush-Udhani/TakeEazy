package duodev.take.eazy.orders.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.OrderItems

class OrdersAdapter(
    private val list: MutableList<OrderItems>
) : BaseRecyclerViewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_order_item, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<OrderItems>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemNameOrder: TextView = itemView.findViewById(R.id.itemName)
        private val itemStatus: TextView = itemView.findViewById(R.id.itemStatus)

        fun bindItems(order: OrderItems) {

            itemNameOrder.text = order.cartItem.singleItem.itemName
            if (order.status == "") {
                itemStatus.text = "Waiting for approval"
            } else {
                itemStatus.text = order.status
            }

        }
    }
}