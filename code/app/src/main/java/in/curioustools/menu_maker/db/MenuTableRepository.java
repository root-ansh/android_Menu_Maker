package in.curioustools.menu_maker.db;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MenuTableRepository {

    private MenuTableAccessDao menuDao;
    private ExecutorService dbQueryExecutorService;

    public static final int DB_PAGE_SIZE = 20;


    public MenuTableRepository(Context appCtx) {
        this.menuDao = MenuDB.getInstance(appCtx).getSongsTableAccessDao();
        this.dbQueryExecutorService = Executors.newSingleThreadExecutor();
    }


    public void insertMenuEntry(@NonNull final MenuEntry... entries) {
        dbQueryExecutorService.execute(() -> menuDao.insertMenuItems(entries));
    }

    public LiveData<PagedList<MenuEntry>> getAllMenuEntriesLivePaged(){
        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setPrefetchDistance(DB_PAGE_SIZE/2)
                        .setPageSize(DB_PAGE_SIZE)
                        .build();
        return new LivePagedListBuilder<>(menuDao.getMenuItemsDataSource(), pagedListConfig).build();
    }

    public void deleteMenuEntry(String menuEntryID) {
        dbQueryExecutorService.execute(() -> menuDao.deleteItemByID(menuEntryID));
    }

    public LiveData<List<MenuEntry>> getAllMenuEntriesLive(){
        return menuDao.getMenuItemsLiveList();
    }



}