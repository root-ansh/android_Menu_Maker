/*
 * Copyright (c) 2020.
 * created on 18/6/20 9:56 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

@file:Suppress("FunctionName", "SpellCheckingInspection")

package `in`.curioustools.menu_maker.modal

import android.content.Context
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.random.Random


//==================main entities===================================================================
// background we have to create a databse for a resteraunt. resteraunt has many categories of items
// like parathas(tawa paratha, gobi paratha, aloo paratha), sabzi(aloo gobhi, mix veg), paneer(shahipaneer) etc
// thus for category:item relation, its 1:many . also, a particular item like shahi paneer can belong to
// multiple categories like both sabzi an paneer, so we there is a item:category relation as 1:many
// The 2 insights above are nough to classify this db as many to many, but i had some more thoughts
//
// So we have some relations as A->B(b1,b2) and some relations as B->A(a1,a2) and since not all
// items are in multiple categories , we can say that "some" items have 1:1 relation. Thus i will
// categorize this relation as "1->1" , "1->many" and "many->1" , but not "many->many", since the
// complete graph isn't exactly connected . i won't even add a "1<-1" connection (note the direction)
// because what if we only have to fetch categories  for  items showing in more than 1 category?
// then having a reverse query won't be good.
//
// But these are all just abstract stuff. if we were to believe google, one:one or many:many are not
// directed relations . if there is a1 directional road from a->b and one directional road from b->a
// this means there is one single bi directional road from a<-->b  . this is the approach we usually
// use. that is, keeping the 2 tables aside and 1 relations table for their one-one or one-many or
// many-many relation.
//
// other than that, we create some kind of helper classes to get data in the format we want. this db
// exhibits the the same way using the modern room libs :
//           //room
//            def room_version = "2.2.5"
//            implementation "androidx.room:room-runtime:$room_version"
//
//            annotationProcessor "androidx.room:room-compiler:$room_version"  //JAVA
//            kapt "androidx.room:room-compiler:$room_version" //KOTLIN
//            implementation "androidx.room:room-ktx:$room_version" //Kotlin Extensions and Coroutines support for Room
//todo :figure out all the practical queries and add it to templates, remove all the theory comments
//

//==================main entities===================================================================

@Entity(tableName = "menu_cat") @Parcelize
data class MenuCategory(
        @ColumnInfo(name = "cat_id") @PrimaryKey
        val id: String = UUID.randomUUID().toString().substring(0, 16),

        @ColumnInfo(name = "cat_name")
        var name: String
) : Parcelable {
    constructor(catName: String) : this(name = catName)

    override fun toString(): String {
        return "MenuCategory(id='$id', name='$name')"
    }
}

@Entity(tableName = "menu_item") @Parcelize
data class MenuItem(
        @ColumnInfo(name = "item_id") @PrimaryKey
        var id: String = UUID.randomUUID().toString().substring(0, 16),

        @ColumnInfo(name = "item_name")
        var itemName: String,

        @ColumnInfo(name = "p_half")
        var priceHalf: Int,

        @ColumnInfo(name = "p_full")
        var priceFull: Int

) : Parcelable {
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

        val items: List<MenuItem> = listOf()

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

        val categories: List<MenuCategory> = listOf()

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

    //todo :livedata or coroutine or rx backed queries for all of these

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


fun getSampleManyManyListCI():List<MenuCategoryWithAssocItems>{

    val items = listOf(
        //Breads
        MenuItem("Naan",25),        MenuItem("Tawa Roti",7),     MenuItem("Tandoori Roti",5),
        MenuItem("Lachha",15),       MenuItem("Aloo Paratha",20),MenuItem("Gobhi Paratha",35),
        MenuItem("Paneer Paratha",25),

        //Daals
        MenuItem("Dal Makhini",50,80), MenuItem("Dal Peeli",40,70), MenuItem("Dal Fry",80),
        MenuItem("Dal Tadks",50,80), MenuItem("Rajma",40,90)/*also a subzi*/, MenuItem("Chhole",50,90),
        MenuItem("Chhole Fry",60,100),


        //Sabzis
        MenuItem("Aloo Gobhi",70,100), MenuItem("kadhi",120), MenuItem("Aloo matar",70,100),

        //Paneers
        MenuItem("Shahi Paneer",90,150), MenuItem("Kadhai Paneer",100,160),
        MenuItem("Aloo Paneer",100,110)/*also a subzi*/,

        //Extras
        MenuItem("Butter",5),

        //Snacks
        MenuItem("Muradabadi Dal",40),/*also a a dal*/
        MenuItem("Pizza",150)
    )
    val cats = listOf(
        MenuCategory("Breads"),
        MenuCategory("Dals"),
        MenuCategory("Sabzi"),
        MenuCategory("Paneer"),
        MenuCategory("Extras"),
        MenuCategory("Snacks")
    )

    return listOf(

        MenuCategoryWithAssocItems(cats[0], items.subList(0,7)),

        MenuCategoryWithAssocItems(cats[1],items.subList(7,14) ),

        MenuCategoryWithAssocItems(cats[2],items.subList(14,17)),

        MenuCategoryWithAssocItems(cats[3],items.subList(17,20)),

        MenuCategoryWithAssocItems(cats[4],items.subList(20,21))

        )



}


fun getSampleCategoriesList()= listOf(
    MenuCategory("Breads"),
    MenuCategory("Dals"),
    MenuCategory("Sabzi"),
    MenuCategory("Paneer"),
    MenuCategory("Extras"),
    MenuCategory("Snacks")
)
fun getSampleItemList() = listOf(
    //Breads
    MenuItem("Naan",25),


    //Daals
    MenuItem("Dal Makhini",50,80),

    //Sabzis
    MenuItem("Aloo Gobhi",70,100),

    //Paneers
    MenuItem("Shahi Paneer",90,150),

    //Extras
    MenuItem("Butter",5),

    //Snacks
    MenuItem("Muradabadi Dal",40),/*also a a dal*/
    MenuItem("Pizza",150)


    )


fun getSampleCatItemOneOneDataCI(){//:List<MenuCategoryWithAssocItems>{
//    // all data is of format Category--Item. no category has 2 items, no item belongs to 2 categories
//    //return type looks like that of category->list<Item>, but that's just a representation
//
//    val items = getSampleItemList()
//    val cats = getSampleCategoriesList()
//
//    return listOf(
//        MenuCategoryWithAssocItems(cats[0], listOf(items[0])),  /* BREADS----[ NAAN ] */
//        MenuCategoryWithAssocItems(cats[1], listOf(items[1])),  /* BREADS----[ NAAN ] */
//        MenuCategoryWithAssocItems(cats[2], listOf(items[2])),  /* BREADS----[ NAAN ] */
//        MenuCategoryWithAssocItems(cats[3], listOf(items[3])),  /* BREADS----[ NAAN ] */
//        MenuCategoryWithAssocItems(cats[4], listOf(items[4])),  /* BREADS----[ NAAN ] */
//        MenuCategoryWithAssocItems(cats[5], listOf(items[6]))   /* EXTRAS----[ PIZZA ] */
//    )
    //todo


}
fun getSampleCatItemOneOneDataIC(){//}:List<MenuItemWithAssocCategories>{
    // all data is of format Category--Item. no category has 2 items, no item belongs to 2 categories
    //return type looks like that of Item->list<Category>, but that's just a representation
//
//    val items = getSampleItemList()
//    val cats = getSampleCategoriesList()
//
//    return listOf(
//        MenuItemWithAssocCategories(items[0], listOf(cats[0])),  /* NAAN----[ BREADS ] */
//        MenuItemWithAssocCategories(items[1], listOf(cats[1])),  /* NAAN----[ BREADS ] */
//        MenuItemWithAssocCategories(items[2], listOf(cats[2])),  /* NAAN----[ BREADS ] */
//        MenuItemWithAssocCategories(items[3], listOf(cats[3])),  /* NAAN----[ BREADS ] */
//        MenuItemWithAssocCategories(items[4], listOf(cats[4])),  /* NAAN----[ BREADS ] */
//        MenuItemWithAssocCategories(items[5], listOf(cats[6]))   /* PIZZA----[ EXTRAS ] */
//    )

    // TODO: 20/06/20

}

fun getSampleCatItemOneManyDataCI(){//:List<MenuCategoryWithAssocItems>{
    // all data is of format Category-->list<Item>. category could have multiple items, but items
    // of one category will not belong to other
//
//    val items = getSampleItemList()
//    val cats = getSampleCategoriesList()
//
//    return listOf(
//        MenuCategoryWithAssocItems(cats[0], listOf(items[0])),
//        MenuCategoryWithAssocItems(cats[1], listOf(items[1])),
//        MenuCategoryWithAssocItems(cats[2], listOf(items[2])),
//        MenuCategoryWithAssocItems(cats[3], listOf(items[3])),
//        MenuCategoryWithAssocItems(cats[4], listOf(items[4])),
//        MenuCategoryWithAssocItems(cats[5], listOf(items[5]))
//    )
    // TODO: 20/06/20

}
fun getSampleCatItemOneManyDataIC(){//:List<MenuItemWithAssocCategories>{
    // TODO: 20/06/20
}





// ====Tests for static dao methods(the one that does not return liv/rx/coroutine responses ========


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
