package `in`.curioustools.menu_maker

import `in`.curioustools.menu_maker.db.MenuEntry
import `in`.curioustools.menu_maker.modal.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
/*
        val it = listOf<MenuEntry>(
            MenuEntry("a"),
            MenuEntry("b"),
            MenuEntry("v"),
            MenuEntry("d"),
            MenuEntry("b"),
            MenuEntry("a"),
            MenuEntry("c"),
            MenuEntry("a")
            )
        println(it)

        val filteredStrings = it.map { it.categoryName };
        println(filteredStrings)
        val filteredList = filteredStrings.distinct() //for reducing to only original strings
        println(filteredList)

        */
        assertEquals(4, 2 + 2)


    }

    @Test
    fun testList(){
        val inpList = listOf(
            MenuCategoryWithAssocItems(
                MenuCategory("cat1"),
                listOf(getRandomMenuItem1Price(), getRandomMenuItem2Price(), getRandomMenuItem1Price())
            )
        )
    }

    fun convert(items:List<MenuCategoryWithAssocItems>):List<Any>{
        val res = mutableListOf<Any>()
        items.forEach {catWithItem->

            res.add(catWithItem.category)

            catWithItem.items.forEach { itm->
                res.add(items) }
        }

        return res.toList()
    }

    @Test
    fun test (){
        val categories = getSampleCategoriesList()
        val categoriesWithBoolean =categories.map { mutableListOf(it,false) }
        categoriesWithBoolean[0][1]=true

        categoriesWithBoolean.forEach {  println(it)}
    }


}