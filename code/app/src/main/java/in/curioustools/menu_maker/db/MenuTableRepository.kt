package `in`.curioustools.menu_maker.db

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Suppress("PrivatePropertyName")
class MenuTableRepository(appCtx: Context, debug: Boolean ) {

    constructor(appCtx: Context) : this(appCtx,false)

    private val menuDao: MenuTableAccessOld? = MenuDB.getInstance(appCtx,debug).menuTableAccessDao
    private val dbQueryExecutorService: ExecutorService = Executors.newSingleThreadExecutor()
    private val DB_PAGE_SIZE = 20


    fun insertMenuEntry(vararg entries: MenuEntry) {
        dbQueryExecutorService.execute { menuDao?.insertMenuItems(*entries) }
    }


    fun deleteMenuEntry(menuEntryID: String?) {
        dbQueryExecutorService.execute { menuDao?.deleteItemByID(menuEntryID!!) }
    }

    fun getAllMenuEntriesLivePaged(): LiveData<PagedList<MenuEntry>>? {
        val pagedListConfig =
            PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(DB_PAGE_SIZE / 2)
                .setPageSize(DB_PAGE_SIZE)
                .build()
        return LivePagedListBuilder(menuDao!!.getMenuItemsDataSource(), pagedListConfig).build()
    }


    fun getAllMenuEntriesLive(): LiveData<List<MenuEntry>>? = menuDao?.getMenuItemsLiveList()

}

/*
*  Option 1 : falling null : that is passing treating every value that could have null as nullable (with ?)
*  and returning outputs accordingly (i.e returning value only if the generator is not null other wise returning null)
*       adv      : less chances of crash since we are still handling the null by ourself
*       dis_adv  : lots of code to handle, not better than the usual java approach
*       trivia   : we could use the elvis operator(?:) to still get a default value if a particular stuff
*                  is generating null as  generator?.getValue()?:default_value
* Option 2  : lateinit /by lazy / using !!
* */