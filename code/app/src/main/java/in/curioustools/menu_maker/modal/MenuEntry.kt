/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:49 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */
package `in`.curioustools.menu_maker.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "menu_entry_table")
data class MenuEntry(
    @ColumnInfo(name = "id") @PrimaryKey
    var id: String = UUID.randomUUID().toString(),

    @ColumnInfo(name = "category_name")
    var categoryName: String = "",

    @ColumnInfo(name = "item_name")
    var itemName: String = "",

    @ColumnInfo(name = "p_half")
    var priceHalf: Int = -1,

    @ColumnInfo(name = "p_full")
    var priceFull: Int = -1,

    @ColumnInfo(name = "type")
    var type: Int = MenuEntryType.CATEGORY.ordinal

) {

    constructor(category: String) : this(categoryName = category,type = MenuEntryType.CATEGORY.ordinal)
    constructor(category: String, itemName: String, priceHalf: Int, priceFull: Int) :
            this(
                categoryName = category,
                itemName = itemName,
                priceHalf = priceHalf,
                priceFull = priceFull,
                type = MenuEntryType.ITEM.ordinal

            )

    constructor(category: String, itemName: String, priceTotal: Int) :
            this(
                categoryName = category,
                itemName = itemName,
                priceFull = priceTotal,
                type = MenuEntryType.ITEM.ordinal

            )


    override fun toString(): String {
        return "MenuEntries{" +
                "id='$id', " +
                "type=$type, " +
                "categoryName='$categoryName', " +
                "itemName='$itemName', " +
                "priceHalf=$priceHalf, " +
                "priceFull=$priceFull" +
                "}"
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + categoryName.hashCode()
        result = 31 * result + itemName.hashCode()
        result = 31 * result + priceHalf
        result = 31 * result + priceFull
        result = 31 * result + type
        return result
    }


}

enum class MenuEntryType {
    CATEGORY, ITEM
}