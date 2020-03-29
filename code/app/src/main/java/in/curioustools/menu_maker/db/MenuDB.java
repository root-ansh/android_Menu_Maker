package in.curioustools.menu_maker.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import in.curioustools.menu_maker.modal.MenuEntry;


@Database(entities = MenuEntry.class,version = 1,exportSchema = false)
public  abstract class MenuDB extends RoomDatabase{
    abstract MenuTableAccessDao getSongsTableAccessDao();

    private static volatile MenuDB INSTANCE = null;
    private static final  String DB_NAME = "MENU.db";

    @NonNull
    static synchronized MenuDB getInstance(final Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, MenuDB.class, DB_NAME).build();
        }
        return INSTANCE;

        //For testing:INSTANCE = Room.inMemoryDatabaseBuilder(context, MenuDB.class).build();

    }


}
