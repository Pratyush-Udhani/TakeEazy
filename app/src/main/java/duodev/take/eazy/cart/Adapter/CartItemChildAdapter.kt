package duodev.take.eazy.cart.Adapter

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
import duodev.take.eazy.utils.getAddedInt
import duodev.take.eazy.utils.getSubInt
import duodev.take.eazy.utils.makeGone

class CartItemChildAdapter (
    private val list: MutableList<CartItems>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_cart_single_item, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                holder.bindItems(list[position])
            }
        }
    }

    fun addData(data: List<CartItems>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemName: TextView = itemView.findViewById(R.id.itemName)
        private val itemCard: CardView = itemView.findViewById(R.id.itemCard)
        private val addQuantity: ImageView = itemView.findViewById(R.id.addQuantityButton)
        private val subQuantity: ImageView = itemView.findViewById(R.id.subQuantityButton)
        private val editQuantity: LinearLayout = itemView.findViewById(R.id.editQuantityLayout)
        private val itemQuantity: TextView = itemView.findViewById(R.id.quantityText)

        fun bindItems(item: CartItems) {
            itemName.text = item.cartItem.itemName
            itemQuantity.text = item.quantity.toString()

            addQuantity.setOnClickListener {
                listener.addToCart(CartItems(item.cartItem, getAddedInt(itemQuantity.text.toString())))
                itemQuantity.text = getAddedInt(itemQuantity.text.toString()).toString()
            }

            subQuantity.setOnClickListener {
                if (itemQuantity.text == "1") {
                    listener.removeFromCart(item.cartItem.itemId)
                    removeAt(adapterPosition)
                } else {
                    listener.subFromCart(CartItems(item.cartItem, getSubInt(itemQuantity.text.toString())))
                    itemQuantity.text = getSubInt(itemQuantity.text.toString()).toString()
                }
            }
        }
    }

    interface OnClick {
        fun addToCart(item: CartItems)
        fun subFromCart(item: CartItems)
        fun removeFromCart(itemId: String)
    }
}