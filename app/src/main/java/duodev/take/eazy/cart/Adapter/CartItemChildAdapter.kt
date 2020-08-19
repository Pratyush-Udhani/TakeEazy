package duodev.take.eazy.cart.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.pojo.CartItems
import duodev.take.eazy.utils.getAddedInt
import duodev.take.eazy.utils.getSubInt

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

    fun removeData() {
        list.removeAll(list)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val itemNameText: TextView = itemView.findViewById(R.id.itemName)
        private val itemImage: ImageView = itemView.findViewById(R.id.itemImage)
        private val addQuantity: TextView = itemView.findViewById(R.id.addQuantityButton)
        private val subQuantity: TextView = itemView.findViewById(R.id.subQuantityButton)
        private val editQuantity: LinearLayout = itemView.findViewById(R.id.editQuantityLayout)
        private val itemQuantity: TextView = itemView.findViewById(R.id.quantityText)

        fun bindItems(item: CartItems) {
            itemNameText.text = item.singleItem.itemName
            itemQuantity.text = item.quantity.toString()

            Glide.with(getContext()).load(item.singleItem.itemImageUri).into(itemImage)

            addQuantity.setOnClickListener {
                listener.addToCart(CartItems(item.singleItem, getAddedInt(itemQuantity.text.toString()), item.storeId))
                itemQuantity.text = getAddedInt(itemQuantity.text.toString()).toString()
            }

            subQuantity.setOnClickListener {
                if (itemQuantity.text == "1") {
                    listener.removeFromCart(item.singleItem.itemId, item.storeId)
                    removeAt(adapterPosition)
                } else {
                    listener.subFromCart(CartItems(item.singleItem, getSubInt(itemQuantity.text.toString()), item.storeId))
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