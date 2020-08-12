package duodev.take.eazy.stores.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.Items

class StoreItemGroupAdapter(
    private val list: MutableList<Items>,
    private val listener: OnItemClicked
) : BaseRecyclerViewAdapter(), StoreItemSingleAdapter.OnClick {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val itemsAdapter by lazy { StoreItemSingleAdapter(mutableListOf(), this) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_store_item_group, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItem(list[position])
        }
    }

    fun addData(data: List<Items>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val group: TextView = itemView.findViewById(R.id.itemGroup)
        private val itemsRecycler: RecyclerView = itemView.findViewById(R.id.itemsRecycler)

        fun bindItem(item: Items) {
            group.text = item.itemGroup
            itemsAdapter.addData(item.itemList)
            itemsRecycler.apply {
                adapter = this@StoreItemGroupAdapter.itemsAdapter
                layoutManager = LinearLayoutManager(itemsRecycler.context)
                setRecycledViewPool(this@StoreItemGroupAdapter.viewPool)
            }
        }
    }

    override fun new(some: String) {
        listener.clickedItem(some)
    }

    interface OnItemClicked {
        fun clickedItem(msg: String)
    }
}