package duodev.take.eazy.home.Adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import duodev.take.eazy.R
import duodev.take.eazy.base.BaseRecyclerViewAdapter
import duodev.take.eazy.utils.*

class CategoryHomeAdapter(
    private val list: MutableList<String>,
    private val listener: OnClick
) : BaseRecyclerViewAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(getView(R.layout.card_categories_home, parent))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> holder.bindItems(list[position])
        }
    }

    fun addData(data: List<String>) {
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryName: TextView = itemView.findViewById(R.id.category)
        private val categoryCard: CardView = itemView.findViewById(R.id.categoryCard)
        private val categoryImage: ImageView = itemView.findViewById(R.id.categoryImage)

        fun bindItems(category: String) {
            categoryName.text = category

            categoryCard.setOnClickListener {
                listener.onCategoryClicked(category)
            }
            val imageDrawable = when(category) {
                GROCERIES -> R.drawable.ic_groceries
                MEDICINES -> R.drawable.ic_medicine
                FRUITS -> R.drawable.ic_fruits_veges
                BOOKS -> R.drawable.ic_books
                MEAT -> R.drawable.ic_fish_meat
                GIFTS -> R.drawable.ic_gifts
                PET -> R.drawable.ic_pet_supplies
                HEALTH -> R.drawable.ic_health
                SWEETS -> R.drawable.ic_sweets
                ELECTRICAL -> R.drawable.ic_electrical_appliances
                else -> R.drawable.ic_body_wash
            }
            categoryImage.setImageResource(imageDrawable)
        }
    }

    interface OnClick {
        fun onCategoryClicked(category: String)
    }
}