package duodev.take.eazy.tobedeleted

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.cart.Adapter.CartItemChildAdapter
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.OrderItems

class CartItemParentAdapter(
    private val list: MutableList<OrderItems>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter(),
    CartItemChildAdapter.OnClick {

    private val viewPool = RecyclerView.RecycledViewPool()
    private val cartChildAdapter by lazy {
        CartItemChildAdapter(
            mutableListOf(),
            this
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_cart_store_items, parent))
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

        private val storeName: TextView = itemView.findViewById(R.id.storeName)
        private val childRecycler: RecyclerView = itemView.findViewById(R.id.itemsCartChildRecycler)

        fun bindItems(item: OrderItems) {
//            storeName.text = item.store.storeName
//            this@CartItemParentAdapter.cartChildAdapter.addData(item.itemList)
            childRecycler.apply {
                adapter = this@CartItemParentAdapter.cartChildAdapter
                layoutManager = LinearLayoutManager(childRecycler.context)
                setRecycledViewPool(this@CartItemParentAdapter.viewPool)
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
        TODO("Not yet implemented")
    }


    interface OnClick {
        fun addToCart(item: CartItems)
        fun subFromCart(item: CartItems)
        fun removeFromCart(itemId: String)
    }
}