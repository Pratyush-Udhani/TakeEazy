package duodev.take.eazy.stores.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.Store

class StoreListAdapter(
    private val list: LinkedHashMap<Store, String>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_store, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(
                list.keys.elementAt(position),
                list.values.elementAt(position)
            )
        }
    }

    fun removeData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun addData(data: MutableMap<Store, String>) {
        list.putAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val storeName: TextView = itemView.findViewById(R.id.storeName)
        private val storeCard: CardView = itemView.findViewById(R.id.storeCard)

        fun bindItems(store: Store, distance: String) {
            storeName.text = store.storeName
            storeCard.setOnClickListener {
                listener.onStoreClicked(store, distance)
            }
        }
    }

    interface OnClick {
        fun onStoreClicked(store: Store, distance: String)
    }
}