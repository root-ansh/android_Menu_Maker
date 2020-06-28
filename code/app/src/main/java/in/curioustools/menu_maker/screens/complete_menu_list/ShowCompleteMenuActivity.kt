/*
 * Copyright (c) 2020.
 * created on 20/6/20 9:19 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.complete_menu_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuCategory
import `in`.curioustools.menu_maker.modal.MenuCategoryWithAssocItems
import `in`.curioustools.menu_maker.modal.MenuItem
import `in`.curioustools.menu_maker.modal.getSampleManyManyListCI
import android.view.Menu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_show_comp_menu.*

class ShowCompleteMenuActivity : AppCompatActivity() {
    var adp: MenuCategoryWithItemsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_comp_menu)
    }

    override fun onStart() {
        super.onStart()

        val singleItemActions = object : SingleItemHolder.SingleItemActions {
            override fun onEditClick(selfData: MenuItem, parent: MenuCategory) {
                Toast.makeText(
                    this@ShowCompleteMenuActivity,
                    "EDIT: itemclicked= $selfData, category=$parent",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDeleteClick(selfData: MenuItem, parent: MenuCategory) {
                Toast.makeText(
                    this@ShowCompleteMenuActivity,
                    "DELETE: itemclicked= $selfData, category=$parent",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }
        val singleCategoryActions = object : SingleItemHolder.SingleCategoryActions {
            override fun onEditClick(selfData: MenuCategoryWithAssocItems) {
                Toast.makeText(
                    this@ShowCompleteMenuActivity,
                    "EDIT cat ${selfData.category.name}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        val datList = getSampleManyManyListCI()
        adp = MenuCategoryWithItemsAdapter(datList, false, singleItemActions, singleCategoryActions)


        rv_menu_entries?.setHasFixedSize(true)
        rv_menu_entries?.layoutManager = LinearLayoutManager(this)
        rv_menu_entries?.adapter = adp

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_entrylist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_pdf -> {
                // TODO: 21/06/20
                true
            }
            R.id.menu_item_edit -> {
                adp?.showButtons = !(adp!!.showButtons)
                adp?.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }


    }


}