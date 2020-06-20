/*
 * Copyright (c) 2020.
 * created on 20/6/20 9:19 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.tmp_testing2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.modal.MenuCategory
import `in`.curioustools.menu_maker.modal.MenuItem
import `in`.curioustools.menu_maker.modal.getSampleManyManyListCI
import android.view.Menu
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_tmp_testing.*

class TmpTestingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tmp_testing)
    }

    override fun onStart() {
        super.onStart()

        val datList = getSampleManyManyListCI()
        val singleItemActions  =  object :SingleItemHolder.SingleItemActions{
            override fun onEditClick(selfData: MenuItem, parent: MenuCategory) {
                Toast.makeText(this@TmpTestingActivity, "EDIT: itemclicked= $selfData, category=$parent", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteClick(selfData: MenuItem, parent: MenuCategory) {
                Toast.makeText(this@TmpTestingActivity, "DELETE: itemclicked= $selfData, category=$parent", Toast.LENGTH_SHORT).show()

            }

        }

        rv_menu_entries?.setHasFixedSize(true)
        rv_menu_entries?.layoutManager = LinearLayoutManager(this)
        rv_menu_entries?.adapter = MenuCategoryWithItemsAdapter(datList,true,singleItemActions)

    }
}