/*
 * Copyright (c) 2020.
 * created on 29/3/20 1:42 PM
 * by  Ansh Sachdeva (www.github.com/root-ansh)
 *
 */

package in.curioustools.menu_maker.screens.preview;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import in.curioustools.menu_maker.db.MenuTableRepository;
import in.curioustools.menu_maker.modal.MenuEntry;

public class PreviewAndConverActivityVM extends AndroidViewModel {
    private MenuTableRepository repo;

    public PreviewAndConverActivityVM(@NonNull Application application) {
        super(application);
        repo = new MenuTableRepository(application.getApplicationContext());

    }

    @Nullable
    public LiveData<List<MenuEntry>> getAllEntriesLivedata() {
        return repo.getAllMenuEntriesLive();
    }


}
