package duodev.take.eazy.stores.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.Store
import duodev.take.eazy.utils.log

class StoreItemGroupAdapter(
    private val list: MutableList<Items>,
    private val listener: OnItemClicked,
    private val store: Store
) : BaseRecyclerViewAdapter(), StoreItemSingleAdapter.OnClick {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val itemsAdapter by lazy { StoreItemSingleAdapter(mutableListOf(), this, store.storePhone) }

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

    fun addData(data: Items) {
        list.add(data)
        notifyDataSetChanged()
    }

    fun removeData() {
        list.clear()
        notifyDataSetChanged()
    }

    fun updateItem(updateItem: CartItems) {
                log("in adapter: ${list.size}")
        list.forEachIndexed { indexList, items ->
                val newItemList : MutableList<CartItems> = mutableListOf()
                items.itemList.forEachIndexed { index, cartItems ->
                    if (cartItems.singleItem.itemId == updateItem.singleItem.itemId) {
                        newItemList.add(updateItem)
                    } else {
                        newItemList.add(cartItems)
                    }
                }
                list[indexList] = Items(items.itemGroup, newItemList)
                notifyItemChanged(indexList)
            }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val group: TextView = itemView.findViewById(R.id.itemGroup)
        private val itemsRecycler: RecyclerView = itemView.findViewById(R.id.itemsRecycler)

        fun bindItem(item: Items) {
            group.text = item.itemGroup
            itemsRecycler.apply {
                adapter = StoreItemSingleAdapter(item.itemList as MutableList<CartItems>, this@StoreItemGroupAdapter, store.storePhone)
                layoutManager = LinearLayoutManager(itemsRecycler.context)
                setRecycledViewPool(this@StoreItemGroupAdapter.viewPool)
            }
        }
    }

    override fun addToCart(item: CartItems) {
        listener.addToCart(item)
    }

    override fun subFromCart(item: CartItems) {
        listener.subFromCart(item)
    }

    override fun removeFromCart(itemId: String, storeId: String) {
    listener.removeFromCart(itemId, storeId)
    }

    interface OnItemClicked {
        fun addToCart(item: CartItems)
        fun subFromCart(item: CartItems)
        fun removeFromCart(itemId: String, storeId: String)
    }
}