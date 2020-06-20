/*
 * Copyright (c) 2020.
 * created on 20/6/20 5:19 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.tmp_testing2

import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuCategory
import `in`.curioustools.menu_maker.modal.MenuCategoryWithAssocItems
import `in`.curioustools.menu_maker.modal.MenuItem
import `in`.curioustools.menu_maker.screens.tmp_testing2.SingleItemHolder.*
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// good help https://medium.com/@ashishkudale/android-list-inside-list-using-recyclerview-73cff2c4ea95
// TODO: 20/06/20  menu category ke liye edit/delete
class MenuCategoryWithItemsAdapter(
    val dataList: List<MenuCategoryWithAssocItems>,  val showButtons: Boolean, val singleItemActions: SingleItemActions
) : RecyclerView.Adapter<SingleCatWithItemsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleCatWithItemsHolder {
        return SingleCatWithItemsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_single_menucat_with_items,parent,false)
        )
    }

    override fun onBindViewHolder(holder: SingleCatWithItemsHolder, pos: Int) {
        holder.bind(showButtons,dataList[pos],singleItemActions)
    }


    override fun getItemCount() = dataList.size

}


class SingleCatWithItemsHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val tvCatName: TextView? = v.findViewById(R.id.tv_cat_name)
    private val rvItemsForSingleCat: RecyclerView? = v.findViewById(R.id.rv_items_for_single_cat)
    private val llRoot: LinearLayout? = v.findViewById(R.id.ll_menu_cat_with_items_root)


    fun bind(
        showButtons:Boolean,
        singleMenuCatItems: MenuCategoryWithAssocItems,
        singleItemActions: SingleItemActions
    ) {

//        val bgRes =
//            when (adapterPosition % 4) {
//                0 -> R.drawable.bg_rect_gradient_prim_primdark_xmlcolor
//                1 -> R.drawable.bg_rect_gradient_prim_primdark_xmlcolor
//                2 -> R.drawable.bg_rect_gradient_prim_primdark_xmlcolor
//                3 -> R.drawable.bg_rect_gradient_prim_primdark_xmlcolor
//                else -> R.drawable.bg_rect_gradient_prim_primdark_xmlcolor
//
//            }
//
//
//        llRoot?.setBackgroundResource(bgRes)

        tvCatName?.text = singleMenuCatItems.category.name

        rvItemsForSingleCat?.layoutManager =
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

        rvItemsForSingleCat?.setHasFixedSize(true)

        val adp = SingleEntryItemsAdapter(singleMenuCatItems, showButtons, singleItemActions)

        rvItemsForSingleCat?.adapter = adp
    }


    // TODO: 20/06/20 somehow pass notify data set change

}


class SingleEntryItemsAdapter(
    private val catWithItems: MenuCategoryWithAssocItems,
    private val showCurrentItemButtons: Boolean,
    private val singleItemActions: SingleItemActions
) : RecyclerView.Adapter<SingleItemHolder>() {

    private val itemList = catWithItems.items

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemHolder {
        return SingleItemHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.layout_single_menuitem, parent, false)
        )
    }


    override fun onBindViewHolder(holder: SingleItemHolder, pos: Int) {
        holder.bind(itemList[pos], singleItemActions, catWithItems.category, showCurrentItemButtons)
    }

    override fun getItemCount() = itemList.size

}


class SingleItemHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val tvItemName: TextView? = v.findViewById(R.id.tv_itm_name)
    private val tvPriceHalf: TextView? = v.findViewById(R.id.tv_itm_price_half)
    private val tvPriceFull: TextView? = v.findViewById(R.id.tv_itm_price_full)
    private val ibtEdit: ImageButton? = v.findViewById(R.id.ibt_itm_edit)
    private val ibtDelete: ImageButton? = v.findViewById(R.id.ibt_itm_delete)


    fun bind(
        selfData: MenuItem,
        itemActions: SingleItemActions,
        parent: MenuCategory,
        showButtons: Boolean
    ) {
        val imgT = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.ic_letter_t_xmldrb_bg_green_xmlcolor
        )
        val imgF = ContextCompat.getDrawable(
            itemView.context,
            R.drawable.ic_letter_f_xmldrb_bg_green_xmlcolor
        )

        tvItemName?.text = selfData.itemName
        tvPriceHalf?.apply {
            if (selfData.priceHalf == -1) {
                visibility = GONE
            } else {
                visibility = VISIBLE
                text = "${selfData.priceHalf} /-"
            }


        }
        tvPriceFull?.apply {

            if (selfData.priceHalf == -1) {
                setCompoundDrawablesWithIntrinsicBounds(imgT, null, null, null)
            } else {
                setCompoundDrawablesWithIntrinsicBounds(imgF, null, null, null)
            }

            text = "${selfData.priceFull} /-"
        }

        ibtEdit?.apply {
            if (showButtons) {
                visibility = VISIBLE
                setOnClickListener { itemActions.onEditClick(selfData, parent) }
            } else {
                setOnClickListener(null)
                visibility = GONE

            }
        }



        ibtDelete?.apply {
            if (showButtons) {
                visibility = VISIBLE
                setOnClickListener { itemActions.onDeleteClick(selfData, parent) }
            } else {
                setOnClickListener(null)
                visibility = GONE

            }
        }

        itemView.setOnClickListener { itemActions.onEditClick(selfData, parent) }


    }


    interface SingleItemActions {
        fun onEditClick(selfData: MenuItem, parent: MenuCategory)
        fun onDeleteClick(selfData: MenuItem, parent: MenuCategory)

    }

}
