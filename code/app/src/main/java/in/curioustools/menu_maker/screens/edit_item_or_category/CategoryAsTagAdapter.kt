/*
 * Copyright (c) 2020.
 * created on 21/6/20 3:58 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.edit_item_or_category

import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuCategory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CategoryAsTagAdapter(var dataList: List<CategoryWithState> = listOf()) :
    RecyclerView.Adapter<CategoryAsTagHolder>() {

    fun setMenuDataList(catWithStateList: List<CategoryWithState>) {
        dataList = catWithStateList
        notifyDataSetChanged()
    }

    var listener: OnTagClickAction ? =null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAsTagHolder {
        return CategoryAsTagHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.layout_single_menucat_as_tag,
                    parent,
                    false
                )
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CategoryAsTagHolder, position: Int) {
        holder.bind(dataList[position], listener)
    }




}


data class CategoryWithState(val category: MenuCategory, var state: Boolean = false)
class CategoryAsTagHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val tvTagName: TextView? = v.findViewById(
        R.id.tv_cat_as_tag
    ) //todo bug here

    fun bind(catWithState: CategoryWithState, tagClickListener: OnTagClickAction?) {
        tvTagName?.text = catWithState.category.name
        if (catWithState.state) {
            tvTagName?.setBackgroundResource(R.drawable.bg_rect_curve16_solid_green_xmlcolor)
            tvTagName?.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.white_fff
                )
            )
        } else {
            tvTagName?.setBackgroundResource(R.drawable.bg_rect_curve16_solid_white_stroke_green_xmlcolor)
            tvTagName?.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.green_5ee
                )
            )
        }

        tvTagName?.setOnClickListener {
            tagClickListener?.onTagClick(catWithState)

        }
    }
}

interface OnTagClickAction {
    fun onTagClick(currentlyClicked: CategoryWithState)
}