package duodev.take.eazy.utils

import duodev.take.eazy.pojo.Items
import duodev.take.eazy.pojo.SingleItem

object DefItems {
    fun getItems(): List<Items> {
        val list: MutableList<Items> = mutableListOf()
        for (i in 0..5) {
        list.add(
            Items(
                itemGroup = "Group $i",
                itemList = getList(i)
            )
        )
        }
        return list
    }

    private fun getList(i: Int): List<SingleItem> {
        val list: MutableList<SingleItem> = mutableListOf()
            list.add(
                SingleItem(
                    itemName = "name $i",
                    itemImageUri = "",
                    itemPrice = ""
                )
            )
        return list
    }
}