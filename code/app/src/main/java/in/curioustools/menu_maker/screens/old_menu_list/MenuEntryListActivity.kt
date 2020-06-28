/*
 * Copyright (c) 2020.
 * created on 29/3/20 5:07 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.screens.old_menu_list

import `in`.curioustools.menu_maker.R
import `in`.curioustools.menu_maker.screens.preview.PreviewAndConvertActivity
import `in`.curioustools.menu_maker.db.MenuEntry
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MenuEntryListActivity : AppCompatActivity() {
    private val TAG = "MenuEntryListAct>>";

    //ui
    private var rvMenu: RecyclerView? = null;
    private var fabAdd: FloatingActionButton? = null
    private var bottomSheetView: LinearLayout? = null
    private var bottomSheetManager: BottomSheetManager? = null

    //uiListeners;
    private var fabClickListener: View.OnClickListener? = null;
    private var rvClickListener: MenuEntryListAdapter.OnMenuEntryClickListener? = null;


    private var adp: MenuEntryListAdapter? = null
    private var viewmodel: MenuEntryListActivityVM? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_entry_list)

        //Always db first then ui, since db happens in async
        initDbVariables();


        initUi();
        initListeners();

    }

    override fun onStart() {
        super.onStart()

        //Always db first then ui, since db happens in async
        startDBStream()

        attachListeners();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_entrylist, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_pdf) {
            openPreviewActivity(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initDbVariables() {
        viewmodel = ViewModelProvider
            .AndroidViewModelFactory(this.application)
            .create(MenuEntryListActivityVM::class.java)
    }

    private fun initUi() {

        bottomSheetView = findViewById(R.id.ll_bottom_sheet_root);
        bottomSheetManager = BottomSheetManager(bottomSheetView, viewmodel);

        rvMenu = findViewById(R.id.rv_MenuListActivity_main);
        rvMenu?.layoutManager = LinearLayoutManager(this)

        fabAdd = findViewById(R.id.fab_add)

        adp = MenuEntryListAdapter()

        rvMenu?.adapter = adp


    }

    private fun initListeners() {
        fabClickListener = View.OnClickListener { bottomSheetManager?.toggleBottomSheet() }

        rvClickListener = object : MenuEntryListAdapter.OnMenuEntryClickListener {
            override fun onDeleteClick(menuEntry: MenuEntry?) {
                if (menuEntry != null) {
                    viewmodel?.deleteItemByID(menuEntry.id)

                } else {
                    Log.e(
                        TAG,
                        "onDeleteClick: could not perform delete operation, current item is null"
                    )
                }
            }

            override fun onEditClick(menuEntry: MenuEntry?) {
                bottomSheetManager?.openForEditing(menuEntry)
            }
        }
    }


    private fun startDBStream() {
        viewmodel?.allEntriesLivePaged?.observe(this, Observer {
            if (it == null) return@Observer;//no idea why

            adp?.submitList(it)
            adp?.notifyDataSetChanged()

            val filteredStrings = it.map { it.categoryName };
            val filteredList = filteredStrings.distinct() 
            //for reducing to only original strings

            bottomSheetManager?.addHintsAdapterForCategory(this, filteredList)

        })
    }

    private fun attachListeners() {
        adp?.clickListener = rvClickListener;
        fabAdd?.setOnClickListener(fabClickListener)

    }


    private fun openPreviewActivity(context: Context) {
        val i = Intent(this, PreviewAndConvertActivity::class.java)
        context.startActivity(i)
    }

    private fun tmpp() {
        val items = listOf(
            MenuEntry("c1"),
            MenuEntry("c1", "p1", 90, 180),
            MenuEntry("c1", "p2", 90, 180),
            MenuEntry("c1", "p3", 90, 180),

            MenuEntry("c1", "p3", 180),
            MenuEntry("c2"),
            MenuEntry("c2", "p1", 90, 180),
            MenuEntry("c2", "p2", 90, 180),
            MenuEntry("c2", "p3", 90, 180),
            MenuEntry("c2", "p3", 180),
            MenuEntry("c2", "p3", 180),


            MenuEntry("c2"),
            MenuEntry("c2", "p1", 90, 180),
            MenuEntry("c2", "p2", 90, 180),

            MenuEntry("c2"),
            MenuEntry("c2", "p1", 90, 180),
            MenuEntry("c2", "p2", 90, 180)
        )
    }


}


