package duodev.take.eazy.stores.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.pojo.SingleItem
import duodev.take.eazy.utils.*

class StoreItemSingleAdapter (
    private val list: MutableList<CartItems>,
    private val listener: OnClick,
    private val storeId: String
) : BaseRecyclerViewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_store_item, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

//    fun addData(data: List<SingleItem>) {
//        list.addAll(data)
//        notifyDataSetChanged()
//    }

    fun updateItem(updateItem: CartItems) {
        val index = list.indexOf(updateItem)
        list[index] = updateItem
        notifyItemChanged(index)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        private val itemCard: CardView = itemView.findViewById(R.id.itemCard)
        private val addToCart: CardView = itemView.findViewById(R.id.addToCart)
        private val addQuantity: ImageView = itemView.findViewById(R.id.addQuantityButton)
        private val subQuantity: ImageView = itemView.findViewById(R.id.subQuantityButton)
        private val editQuantity: LinearLayout = itemView.findViewById(R.id.editQuantityLayout)
        private val itemQuantity: TextView = itemView.findViewById(R.id.quantityText)

        fun bindItems(cartItem: CartItems) {
            itemName.text = cartItem.singleItem.itemName
            itemQuantity.text = cartItem.quantity.toString()

            addToCart.setOnClickListener {
                editQuantity.makeVisible()
                addToCart.makeGone()
                listener.addToCart(CartItems(cartItem.singleItem, getAddedInt(itemQuantity.text.toString()), storeId))
                cartItem.quantity = getAddedInt(itemQuantity.text.toString())
                itemQuantity.text = getAddedInt(itemQuantity.text.toString()).toString()
            }

            addQuantity.setOnClickListener {
                listener.addToCart(CartItems(cartItem.singleItem, getAddedInt(itemQuantity.text.toString()), storeId))
                cartItem.quantity = getAddedInt(itemQuantity.text.toString())
                itemQuantity.text = getAddedInt(itemQuantity.text.toString()).toString()
            }

            subQuantity.setOnClickListener {
                if (itemQuantity.text == "1"){
                    listener.removeFromCart(cartItem.singleItem.itemId, storeId)
                    addToCart.makeVisible()
                    editQuantity.makeGone()
                    cartItem.quantity = 0
                    itemQuantity.text = "0"
                } else {
                listener.subFromCart(CartItems(cartItem.singleItem, getSubInt(itemQuantity.text.toString()), storeId))
                cartItem.quantity = getSubInt(itemQuantity.text.toString())
                itemQuantity.text = getSubInt(itemQuantity.text.toString()).toString()
                }
            }
        }
    }

    interface OnClick {
        fun addToCart(item: CartItems)
        fun subFromCart(item: CartItems)
        fun removeFromCart(itemId: String, storeId: String)
    }
}