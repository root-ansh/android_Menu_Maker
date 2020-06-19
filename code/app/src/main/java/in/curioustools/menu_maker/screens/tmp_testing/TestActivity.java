/*
 * Copyright (c) 2020.
 * created on 19/6/20 8:44 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package in.curioustools.menu_maker.screens.tmp_testing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import in.curioustools.menu_maker.R;
import in.curioustools.menu_maker.modal.ItemCategoryRelation;
import in.curioustools.menu_maker.modal.MenuActionsDao;
import in.curioustools.menu_maker.modal.MenuCategory;
import in.curioustools.menu_maker.modal.MenuCategoryWithAssocItems;
import in.curioustools.menu_maker.modal.MenuDB2;
import in.curioustools.menu_maker.modal.MenuItem;
import in.curioustools.menu_maker.modal.MenuItemWithAssocCategories;

import static in.curioustools.menu_maker.modal.MenuKt.getRandomMenuCategory;
import static in.curioustools.menu_maker.modal.MenuKt.getRandomMenuItem1Price;
import static in.curioustools.menu_maker.modal.MenuKt.getRandomMenuItem2Price;

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
