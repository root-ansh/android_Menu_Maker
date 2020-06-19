/*
 * Copyright (c) 2020.
 * created on 17/6/20 8:00 PM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker.db


import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


@Dao
abstract class MenuTableAccessOld {

    //tested
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMenuItems(vararg  entries: MenuEntry)

    @Query("DELETE FROM menu_entry_table WHERE id = :itemID")
    abstract fun deleteItemByID(itemID:String)

    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
    abstract fun getMenuItems():List<MenuEntry>

    @Query("SELECT DISTINCT category_name FROM menu_entry_table ORDER BY category_name , p_full")
    abstract fun getMenuCategories():List<String>

    @Query("SELECT * FROM menu_entry_table WHERE  category_name = :cat")
    abstract fun getMenuItemsForAParticularCategory(cat:String):List<MenuEntry>





    // untestable without livedata and paging test helpers
    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
    abstract fun getMenuItemsDataSource():DataSource.Factory<Int, MenuEntry>
    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
    abstract fun getMenuItemsLiveList():LiveData<List<MenuEntry>>



//    @Query("SELECT DISTINCT category_name FROM menu_entry_table ORDER BY category_name , p_full")
//    fun getMenuCategoriesStringList(): List<String>

//
//    @Query("SELECT * FROM menu_item WHERE  associatedCatID = :catID")
//    fun getAllMenuItemsForAParticularCategoryID(catID: String): List<MenuItem>
//
//
//
//    @Query("SELECT * FROM menu_item WHERE  associatedCatID = :catID")
//    fun getAllMenuItemsForAParticularCategoryName(catID: String): List<MenuItem>
//
//
//    // untestable without livedata and paging test helpers
//    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
//    fun getMenuItemsDataSource(): DataSource.Factory<Int, MenuEntry> //todo figure out how
//
//    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
//    fun getMenuItemsLiveList(): LiveData<List<MenuEntry>>



}