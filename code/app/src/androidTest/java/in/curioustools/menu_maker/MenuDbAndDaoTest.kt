/*
 * Copyright (c) 2020.
 * created on 18/6/20 2:12 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package `in`.curioustools.menu_maker

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
