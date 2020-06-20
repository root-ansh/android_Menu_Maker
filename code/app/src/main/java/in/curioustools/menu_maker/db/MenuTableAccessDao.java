package in.curioustools.menu_maker.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface MenuTableAccessDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMenuItems(MenuEntry... entries);

    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
    DataSource.Factory<Integer, MenuEntry> getMenuItemsDataSource();

    @Query("DELETE FROM menu_entry_table WHERE id = :itemID")
    void deleteItemByID(String itemID);

    @Query("SELECT * FROM menu_entry_table ORDER BY category_name , p_full")
    LiveData<List<MenuEntry>> getMenuItemsLiveList();


}

