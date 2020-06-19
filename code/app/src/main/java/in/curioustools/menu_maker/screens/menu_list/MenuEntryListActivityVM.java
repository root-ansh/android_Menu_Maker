/*
 * Copyright (c) 2020.
 * created on 29/3/20 11:27 AM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package in.curioustools.menu_maker.screens.menu_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import in.curioustools.menu_maker.db.MenuTableRepository;
import in.curioustools.menu_maker.db.MenuEntry;

public class MenuEntryListActivityVM extends AndroidViewModel {
    private MenuTableRepository repo;

    public MenuEntryListActivityVM(@NonNull Application application) {
        super(application);
        repo = new MenuTableRepository(application.getApplicationContext());

    }

    public void insertOrUpdateMenuItems(@NonNull MenuEntry entry) {
        repo.insertMenuEntry(entry);
    }

     @Nullable
    public LiveData<PagedList<MenuEntry>> getAllEntriesLivePaged() {
        return repo.getAllMenuEntriesLivePaged();
    }

    public void deleteItemByID(@NonNull String itemID) {
        repo.deleteMenuEntry(itemID);

    }



}
