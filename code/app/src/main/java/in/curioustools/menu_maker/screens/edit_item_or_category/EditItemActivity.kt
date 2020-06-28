/*
 * Copyright (c) 2020.
 * created on 21/6/20 3:56 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.edit_item_or_category

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.*
import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {

    enum class EditType { M_CAT, M_ITEM }

    var currentEditType = EditType.M_ITEM

    var initData: Any? = null

    private var tagClickListener: OnTagClickAction? = null
    var checkListener: RadioGroup.OnCheckedChangeListener? = null
    private val adp = CategoryAsTagAdapter(listOf())

    companion object {
        const val KEY_EDIT_TYPE = "edit_type"
        const val KEY_HAS_INIT_DATA = "has_init_data"
        const val KEY_INIT_DATA = "init_data"

        //utils
        private fun attachDefaultStateToCategory(categoryList: List<MenuCategory>): List<CategoryWithState> {
            val categoriesWithBoolean = mutableListOf<CategoryWithState>()
            categoryList.forEach { categoriesWithBoolean.add(CategoryWithState(it)) }
            return categoriesWithBoolean
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        getIntentData()
        setupUI()
        initListeners()
    }

    private fun getIntentData() {

        //null case (defaults to item)
        currentEditType =
            EditType.valueOf(intent.getStringExtra(KEY_EDIT_TYPE) ?: EditType.M_ITEM.name)

        initData = if (intent.getBooleanExtra(KEY_HAS_INIT_DATA, false)) {
            null
        } else {
            intent.getParcelableExtra(KEY_INIT_DATA)
        }

        //item case
      //  currentEditType = EditType.M_ITEM
        //initData = getRandomMenuItem2Price()//getRandomMenuItem1Price() //getRandomMenuItem2Price()

        currentEditType = EditType.M_CAT
         initData = getRandomMenuCategory()

    }

    private fun initListeners() {
        // TODO: 21/06/20

    }

    private fun setupUI() {

        //just the layout for now, no adapter setup
        val fbLayout = FlexboxLayoutManager(this);
        fbLayout.justifyContent = JustifyContent.FLEX_START
        fbLayout.flexDirection = FlexDirection.ROW
        rv_cat_As_tags?.layoutManager = fbLayout


    }


    override fun onStart() {
        super.onStart()

        setDataOnUi()
        startListeners()

    }

    @SuppressLint("ResourceAsColor")
    private fun setDataOnUi() {
        //default data
        currentEditType = EditType.M_ITEM
        var name = ""
        var isSinglePrice = true
        var pHalf = -1
        var pFull = -1
        var catEditData = ""

        //if initData is not null (either menu item or category
        when (initData) {
            null -> {
            }
            is MenuItem -> {
                currentEditType = EditType.M_ITEM
                name = (initData as MenuItem).itemName
                isSinglePrice = (initData as MenuItem).priceHalf == -1
                pHalf = (initData as MenuItem).priceHalf
                pFull = (initData as MenuItem).priceFull
            }
            is MenuCategory -> {
                currentEditType = EditType.M_CAT
                catEditData = (initData as MenuCategory).name

            }
        }

        when (currentEditType) {
            EditType.M_CAT -> {

                updateUiForEditTypeChange()

                et_cat_name?.setText(catEditData)
            }
            EditType.M_ITEM -> {
                updateUiForEditTypeChange()

                var categories = getCategoriesFromDB()
                rv_cat_As_tags?.adapter = adp
                adp.setMenuDataList(attachDefaultStateToCategory(categories))

                et_itm_name?.setText(name)

                //init data
                rbt_itm_single.isChecked = isSinglePrice
                rbt_itm_multi.isChecked = !isSinglePrice


                et_itm_p_half?.setText("${if (pHalf != -1) pHalf else ""}")
                et_itm_p_full?.setText("${if (pFull != -1) pFull else ""}")
                et_itm_p_half?.visibility = if (isSinglePrice) GONE else VISIBLE


            }
        }


    }

    private fun updateUiForEditTypeChange() {
        //change colors of button, change background wallpaper, change ui from item to category, change lower button names
        when (currentEditType) {
            EditType.M_CAT -> {
                tv_add_category?.setBackgroundResource(R.drawable.bg_rect_curve4_solid_green_xmlocolor)
                tv_add_category?.setTextColor(ContextCompat.getColor(this,R.color.white_fff))

                tv_add_item?.setBackgroundResource(R.drawable.bg_rect_curve4_solid_white_xmlcolor)
                tv_add_item?.setTextColor(ContextCompat.getColor(this,R.color.black_000))

                ll_cat?.visibility = VISIBLE
                ll_item?.visibility = GONE
                fr_root?.setBackgroundResource(R.drawable.bg_gradient_half_primary_white_xmlcolors)

                tv_save_item_or_cat?.text = "Save Category"
                tv_delete_item_or_cat?.text = "Delete Category"


            }
            EditType.M_ITEM -> {

                tv_add_category?.setBackgroundResource(R.drawable.bg_rect_curve4_solid_white_xmlcolor)
                tv_add_category?.setTextColor(ContextCompat.getColor(this,R.color.black_000))
                tv_add_item?.setBackgroundResource(R.drawable.bg_rect_curve4_solid_green_xmlocolor)
                tv_add_item?.setTextColor(ContextCompat.getColor(this,R.color.white_fff))
                ll_cat?.visibility = GONE
                ll_item?.visibility = VISIBLE
                fr_root?.setBackgroundResource(R.drawable.bg_rect_gradient_prim_primdark_xmlcolor)

                tv_save_item_or_cat?.text = "Save Item"
                tv_delete_item_or_cat?.text = "Delete Item"



            }
        }
    }


    private fun startListeners() {
        tagClickListener = object : OnTagClickAction {
            override fun onTagClick(currentlyClicked: CategoryWithState) {

                val tagList = adp.dataList
                for (itm in tagList.indices){

                    if(tagList[itm].category.id == currentlyClicked.category.id){
                        tagList[itm].state = !(tagList[itm].state)

                    }
                }

                Log.e("EDitITEM<>>>>>", "onTagClick: current list = $tagList" )
                adp.dataList =tagList
                adp.notifyDataSetChanged()




                Log.e("EDitITEM<>>>>>", "onTagClick: clickedItem $currentlyClicked")


            }
        }

        checkListener = RadioGroup.OnCheckedChangeListener { _, selectedID ->
            when (selectedID) {
                R.id.rbt_itm_single -> {
                    et_itm_p_half?.visibility = GONE
                    et_itm_p_full?.setText("")
                    et_itm_p_full?.hint = "Total Price"
                }
                R.id.rbt_itm_multi -> {
                    et_itm_p_half?.visibility = VISIBLE
                    et_itm_p_full?.setText("")
                    et_itm_p_full?.hint = "Enter Price for Full"
                }

            }
        }

        val tvCategoryClickListener = View.OnClickListener {
            currentEditType = EditType.M_CAT
            updateUiForEditTypeChange()
        }
        val tvItemClickListener = View.OnClickListener {
            currentEditType = EditType.M_ITEM
            updateUiForEditTypeChange()
        }

        adp.listener = tagClickListener
        rg_itm_rates?.setOnCheckedChangeListener(checkListener)
        tv_add_item?.setOnClickListener(tvItemClickListener)
        tv_add_category?.setOnClickListener(tvCategoryClickListener)


        // TODO: 21/06/20  todo button save and button delete click

    }


    private fun getCategoriesFromDB(): List<MenuCategory> {
        return getSampleCategoriesList()
        // TODO: 21/06/20 get from the sql db
    }

}


