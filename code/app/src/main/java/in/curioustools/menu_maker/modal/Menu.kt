/*
 * Copyright (c) 2020.
 * created on 18/6/20 9:56 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

@file:Suppress("FunctionName")

package `in`.curioustools.menu_maker.modal

import android.content.Context
import androidx.room.*
import java.util.*
import kotlin.random.Random


//==================main entities===================================================================
// background we have to create a databse for a resteraunt. resteraunt has many categories of items
// like parathas(tawa paratha, gobi paratha, aloo paratha), sabzi(aloo gobhi, mix veg), paneer(..) etc
// thus for category:item relation, its 1:many . also, a particular item like paneer can belong to
// multiple


@Entity(tableName = "menu_cat")
data class MenuCategory(
        @ColumnInfo(name = "cat_id") @PrimaryKey
        val id: String = UUID.randomUUID().toString().substring(0, 16),

        @ColumnInfo(name = "cat_name")
        var name: String
) {
    constructor(catName: String) : this(name = catName)

    override fun toString(): String {
        return "MenuCategory(id='$id', name='$name')"
    }
}

@Entity(tableName = "menu_item")
data class MenuItem(
        @ColumnInfo(name = "item_id") @PrimaryKey
        var id: String = UUID.randomUUID().toString().substring(0, 16),

        @ColumnInfo(name = "item_name")
        var itemName: String,

        @ColumnInfo(name = "p_half")
        var priceHalf: Int,

        @ColumnInfo(name = "p_full")
        var priceFull: Int

) {
    constructor(name: String, pHalf: Int, pFull: Int) : this(itemName = name, priceHalf = pHalf, priceFull = pFull)
    constructor(itemName: String, priceTotal: Int) : this(itemName = itemName, priceHalf = -1, priceFull = priceTotal)

    override fun toString(): String {
        return "Item{" +
                "id='$id', " +
                "itemName='$itemName', " +
                "priceHalf=$priceHalf, " +
                "priceFull=$priceFull" +
                "}"
    }
}

//==========================relation entities ======================================================

@Entity(tableName = "item_cat_relation", primaryKeys = ["cat_id", "item_id"], indices = [Index("cat_id"), Index("item_id")])
data class ItemCategoryRelation(
        val cat_id: String,  //warning(?) :exact names as parent columns
        val item_id: String
) {
    override fun toString() = "ItemCategoryRelation(categor_id='$cat_id', i_id='$item_id')"
}

//====================helper/ accessor classes for relations========================================
data class MenuCategoryWithAssocItems(
        @Embedded
        val category: MenuCategory, //parent object
        @Relation(
                parentColumn = "cat_id",
                entityColumn = "item_id",
                associateBy = Junction(
                        value = ItemCategoryRelation::class, parentColumn = "cat_id", entityColumn = "item_id"
                )
        )

        val items: List<MenuItem>

) {
    override fun toString() = "N_Cat(name=${category.name}, items=${items.map { it.itemName }})"//Nested Category
}
data class MenuItemWithAssocCategories(
        @Embedded
        val item: MenuItem, //parent object
        @Relation(
                parentColumn = "item_id",
                entityColumn = "cat_id",
                associateBy = Junction(
                        value = ItemCategoryRelation::class, parentColumn = "item_id", entityColumn = "cat_id"
                )
        )

        val categories: List<MenuCategory>

) {
    override fun toString() = "N_Item(name=${item.itemName}, Categories=${categories.map { it.name }})"
}

//=================================access dao ======================================================

@Dao
interface MenuActionsDao {

    //insert,delete ,getall for Menu ITEM
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuItems(vararg items: MenuItem)

    @Query("DELETE FROM menu_item WHERE item_id = :id") //order by??
    fun deleteMenuItemByID(id: String)

    @Query("SELECT * FROM menu_item")
    fun getAllMenuItems(): List<MenuItem>


    //insert,delete ,getall for Menu CATEGORY

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenuCategories(vararg categories: MenuCategory)

    @Query("DELETE FROM menu_cat WHERE cat_id = :id")
    fun deleteMenuCategoryByID(id: String)

    @Query("SELECT * FROM menu_cat")
    fun getAllMenuCategories(): List<MenuCategory>


    //relational queries

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItemCategoryRelation(vararg rel: ItemCategoryRelation)

    @Transaction
    @Query("SELECT * FROM menu_cat")
    fun getAllMenuCategoriesWithItems(): List<MenuCategoryWithAssocItems>


    //new
    @Transaction
    @Query("SELECT * FROM menu_item")
    fun getAllMenuItemsWithCategory(): List<MenuItemWithAssocCategories>

    @Query("DELETE FROM item_cat_relation WHERE cat_id = :catID AND item_id = :itemID")
    fun deleteItemCategoryRelation( catID: String,itemID: String)

//    todo : can't figure out how
//    fun getAllItems_JustTheItem_ForCategoryID(catID: String): List<MenuItem>
//    fun getAllItems_Complete_ForCategoryID(catID: String): List<MenuItemWithAssocCategories>

//    fun getAllCategories_JustTheCategory_ForItemID(catID: String): List<MenuCategory>
//    fun getAllCategories_Complete_ForItemID(catID: String): List<MenuCategoryWithAssocItems>

//    fun insertItemAndAssocCategories(itemWithCategories: MenuItemWithAssocCategories)
//    fun insertCategoryAndAssocItems(categoryWithItems: MenuCategoryWithAssocItems)


}

//=====================================db class ====================================================

@Database(
        entities = [MenuItem::class, ItemCategoryRelation::class, MenuCategory::class],
        version = 3
        /* ,exportSchema = false*/
)
abstract class MenuDB2 : RoomDatabase() {

    abstract val getMenuActionsDao: MenuActionsDao?

    companion object {
        private var INSTANCE: MenuDB2? = null
        private const val DB_NAME = "MENU.db"

        @JvmStatic
        @Synchronized
        fun getInstance(context: Context, debug: Boolean = false): MenuDB2 {

            if (debug) {
                return Room.inMemoryDatabaseBuilder(context, MenuDB2::class.java).build()
            }
            if (INSTANCE == null) {
                INSTANCE =
                        Room
                                .databaseBuilder(context, MenuDB2::class.java, DB_NAME)
                                .fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!

        }
    }
}

//==================================util functions =================================================

fun getRandomMenuCategory() = MenuCategory("cat_${UUID.randomUUID().toString()}".substring(0, 8))
fun getRandomMenuItem2Price(): MenuItem {
    val rnd = Random.nextInt(0, 1000) % 200
    return MenuItem("itm_$rnd", rnd, rnd + 50)
}
fun getRandomMenuItem1Price(): MenuItem {
    val rnd = Random.nextInt(0, 1000) % 200
    return MenuItem("itm_$rnd", rnd + 50)
}


// =========================================instrument test runner(does not run sometimes) =========

/*
@RunWith(AndroidJUnit4::class)
class MenuDbAndDaoTest {
    private var menuDb :MenuDB2? = null
    private  var tableDao  : MenuActionsDao? =null

    @Before
    fun initDb(){
        menuDb    = MenuDB2.getInstance(InstrumentationRegistry.getInstrumentation().context,debug = true)
        tableDao = menuDb?.getMenuActionsDao

    }

    @Test
    fun test() {

        val cat1 = getRandomMenuCategory()
        val cat2 = getRandomMenuCategory()
        val cat3 = getRandomMenuCategory()

        val itm1 = getRandomMenuItem1Price()
        val itm2 = getRandomMenuItem2Price()
        val itm3 = getRandomMenuItem2Price()

        loggy(cat1)
        loggy(cat2)
        loggy(cat3)
        loggy(itm1)
        loggy(itm2)
        loggy(itm3)



        tableState()
        loggy("=========insert Test================================================")

        //insert Test

        tableDao?.insertMenuItems(itm1)
        tableDao?.insertMenuItems(itm2)
        tableDao?.insertMenuItems(itm3)

        tableState()

        loggy("---------------------------------------")

        tableDao?.insertMenuCategories(cat1)
        tableDao?.insertMenuCategories(cat2)
        tableDao?.insertMenuCategories(cat3)
        tableState()
        loggy("---------------------------------------")

        tableDao?.insertItemCategoryRelation(ItemCategoryRelation(cat1.id,itm1.id))
        tableDao?.insertItemCategoryRelation(ItemCategoryRelation(cat2.id,itm2.id))
        tableDao?.insertItemCategoryRelation(ItemCategoryRelation(cat3.id,itm3.id))
        tableState()


        loggy("===========delete test================================================")

        //delete
        tableDao?.deleteMenuCategoryByID(cat1.id)
        tableState()
        tableDao?.deleteMenuCategoryByID(cat2.id)
        tableState()
        tableDao?.deleteMenuCategoryByID(cat3.id)
        tableState()

        tableDao?.deleteMenuItemByID(cat1.id)



        loggy("====================================================================")


    }

    private fun tableState() {
        loggy("GET ALL CATEGORIES:")
        tableDao?.getAllMenuCategories()?: listOf("Empty menu categories").forEach{loggy(it)}

        loggy("GET ALL ITEMS:")
        tableDao?.getAllMenuItems()?: listOf("Empty items").forEach{loggy(it)}

        loggy("GET ALL CATEGORIES WITH ITEMS")
        tableDao?.getAllMenuCategoriesWithItems()?: listOf("Empty categories with items").forEach{loggy(it)}

    }
    private fun loggy(obj:Any ) = Log.e("MenuDbTest>>>", "$obj" )


    @After
    fun closeDb(){
        menuDb?.close()
    }


}
*/


//==================actual activity test (java Works better)========================================

/*

public class TestActivity extends AppCompatActivity {
    MenuActionsDao tableDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        tableDao = MenuDB2.getInstance(this, false).getGetMenuActionsDao();

        new Thread(this::test).start();


    }

    private void test() {


        MenuCategory cat1 = getRandomMenuCategory();
        MenuCategory cat2 = getRandomMenuCategory();
        MenuCategory cat3 = getRandomMenuCategory();
        MenuItem itm1 = getRandomMenuItem1Price();
        MenuItem itm2 = getRandomMenuItem2Price();
        MenuItem itm3 = getRandomMenuItem2Price();

        loggy(cat1);
        loggy(cat2);
        loggy(cat3);

        loggy(itm1);
        loggy(itm2);
        loggy(itm3);

        loggy("======================initialiyy================");
        tableState();

        loggy("=========insert Test================================================");

        //insert Test
        loggy("---------inserting items 1,2 ,3--------------------");

        tableDao.insertMenuItems(itm1);
        tableDao.insertMenuItems(itm2);
        tableDao.insertMenuItems(itm3);
        tableState();

        loggy("---------inserting cat 1,2 ,3--------------------");

        tableDao.insertMenuCategories(cat1);
        tableDao.insertMenuCategories(cat2);
        tableDao.insertMenuCategories(cat3);
        tableState();


        loggy("---------inserting relations c1-i1 (one:one)--------------------");

        // one -one relation
        tableDao.insertItemCategoryRelation(new ItemCategoryRelation(cat1.getId(), itm1.getId()));
        tableState();


        loggy("---------inserting relations c2-i2, c2-i3 (one:many and one:one = many to any)--------------------");        // many-many relation( item2[cat2,cat3])  // could not create a one-many
        // many-many relation( cat2[itm2,itm3])  // could not create a one-many
        // relationship here, since the links are 2 sided
        // in the true sense its actually one-->many and one<--one till now cat2[item2,itm3] but all items are still mapped to 0or 1 category
        tableDao.insertItemCategoryRelation(new ItemCategoryRelation(cat2.getId(), itm2.getId()));
        tableDao.insertItemCategoryRelation(new ItemCategoryRelation(cat2.getId(), itm3.getId()));
        tableState();


        loggy("---------inserting relations c3i3 (one:many + many:one + one:one = many to any)--------------------");        // many-many relation( item2[cat2,cat3])  // could not create a one-many
        // this will make c2[i2,i3] and i1[c1,c3] . now a entities of both table share either one->one relation or one->many relation
        tableDao.insertItemCategoryRelation(new ItemCategoryRelation(cat3.getId(), itm1.getId()));
        tableState();



        loggy("===========delete test================================================");

        //delete

        loggy("----------------removing cat1-----------------------------------");

        // this will not only remove the category but also the links associated with category
        // thus itm1 will no longer remain connected to cat1 and  will be i1[c3]
        tableDao.deleteMenuCategoryByID(cat1.getId());
        tableState();

        loggy("----------------removing itm2-----------------------------------");
        tableDao.deleteMenuItemByID(itm2.getId());
        tableState();

        loggy("----------------removing relation c2i3-----------------------------------");
        //this will make both c2 and i3 completely relationfree as they both had 1 relaion, ie this one
        tableDao.deleteItemCategoryRelation(cat2.getId(),itm3.getId());
        tableState();




        loggy("====================================================================");


    }

    public void tableState() {
        if (tableDao == null) {
            loggy("table dao is null returning");
            return;
        }
        loggy("GET ALL CATEGORIES:");
        for (MenuCategory cat : tableDao.getAllMenuCategories()) {
            loggy(cat);
        }

        loggy("GET ALL ITEMS:");
        for (MenuItem itm : tableDao.getAllMenuItems()) {
            loggy(itm);
        }

        loggy("GET ALL CATEGORIES WITH ITEMS");
        for (MenuCategoryWithAssocItems catItms : tableDao.getAllMenuCategoriesWithItems()) {
            loggy(catItms);
        }

        loggy("GET ALL ITEMS WITH CATEGORIES");
        for (MenuItemWithAssocCategories itmCats : tableDao.getAllMenuItemsWithCategory()) {
            loggy(itmCats);
        }


    }

    public void loggy(Object obj) {
        Log.e("MenuDbTest>>>", "" + obj);
    }

}


* */
