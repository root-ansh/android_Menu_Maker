package `in`.curioustools.menu_maker.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MenuEntry::class], version = 2, exportSchema = false)
abstract class MenuDB : RoomDatabase() {
    abstract val songsTableAccessDao: MenuTableAccessDao?

    companion object {
        @Volatile
        private var INSTANCE: MenuDB? = null
        private const val DB_NAME = "MENU.db"

        @JvmStatic @Synchronized
        fun getInstance(context: Context?): MenuDB {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context!!,
                    MenuDB::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
            }
            return INSTANCE!!

            //For testing:INSTANCE = Room.inMemoryDatabaseBuilder(context, MenuDB.class).build();
        }
    }
}