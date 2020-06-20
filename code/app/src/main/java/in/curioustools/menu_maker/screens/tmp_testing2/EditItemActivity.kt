/*
 * Copyright (c) 2020.
 * created on 20/6/20 10:45 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.tmp_testing2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuCategory
import `in`.curioustools.menu_maker.modal.getSampleCategoriesList
import `in`.curioustools.menu_maker.modal.getSampleManyManyListCI
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
    }

    override fun onStart() {
        super.onStart()


        val categories = getSampleCategoriesList()
        val categoriesWithBoolean = mutableListOf<CategoryWithState>()
        categories.forEach { categoriesWithBoolean.add(CategoryWithState(it)) }

        val listener = object : OnTagClickAction {
            override fun onTagClick(currentlyClicked: CategoryWithState) {

                val adp = rv_cat_As_tags?.adapter as CategoryAsTagAdapter

                val catWithState = adp.dataList
                catWithState.forEach {
                    if (it.category.id.equals(currentlyClicked.category.id)) {
                        it.state = !it.state
                    }
                }

                adp.dataList =catWithState

                Log.e("EDitITEM<>>>>>", "onTagClick: clickedItem $currentlyClicked")


            }
        }
        val adp = CategoryAsTagAdapter(categoriesWithBoolean, listener)

        rv_cat_As_tags?.adapter = adp

        val fbLayout = FlexboxLayoutManager(this);
        fbLayout.justifyContent =JustifyContent.FLEX_START
        fbLayout.flexDirection = FlexDirection.ROW
        rv_cat_As_tags?.layoutManager = fbLayout


        //RecyclerView recyclerView = (RecyclerView) context.findViewById(R.id.recyclerview);
        //FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
        //layoutManager.setFlexDirection(FlexDirection.COLUMN);
        //layoutManager.setJustifyContent(JustifyContent.FLEX_END);
        //recyclerView.setLayoutManager(layoutManager);


    }

}

class CategoryAsTagAdapter(data: List<CategoryWithState> = listOf(), actions: OnTagClickAction) :
    RecyclerView.Adapter<CategoryAsTagHolder>() {

    var dataList: List<CategoryWithState> = data
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var listener: OnTagClickAction? = actions
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAsTagHolder {
        return CategoryAsTagHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_single_menucat_as_tag, parent, false)
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: CategoryAsTagHolder, position: Int) {
        holder.bind(dataList[position], listener!!)
    }

}


data class CategoryWithState(val category: MenuCategory, var state: Boolean = false)

class CategoryAsTagHolder(v: View) : RecyclerView.ViewHolder(v) {
    private val tvTagName: TextView? = v.findViewById(R.id.tv_cat_as_tag) //todo bug here

    fun bind(catWithState: CategoryWithState, tagClickListener: OnTagClickAction) {
        tvTagName?.text = catWithState.category.name
        if (catWithState.state) {
            tvTagName?.setBackgroundResource(R.drawable.bg_rect_curve16_solid_green_xmlcolor)
            tvTagName?.setTextColor(ContextCompat.getColor(itemView.context, R.color.white_fff))
        } else {
            tvTagName?.setBackgroundResource(R.drawable.bg_rect_curve16_stroke_green_xmlcolor)
            tvTagName?.setTextColor(ContextCompat.getColor(itemView.context, R.color.green_5ee))
        }

        tvTagName?.setOnClickListener {
//            catWithState.state = !catWithState.state
            tagClickListener.onTagClick(catWithState)
        }
    }
}

interface OnTagClickAction {
    fun onTagClick(currentlyClicked: CategoryWithState)
}